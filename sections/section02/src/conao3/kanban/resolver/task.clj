(ns conao3.kanban.resolver.task
  (:require
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [next.jdbc :as jdbc]))

(defn tasks [context _args _parent]
  (let [datasource (-> context :db :datasource)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/order-by [:created_at :desc])
             sql/format)
         (jdbc/execute! datasource))))

(defn task [context args _parent]
  (let [datasource (-> context :db :datasource)
        task-id (:task_id args)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/where [:= :task_id [:cast task-id :uuid]])
             sql/format)
         (jdbc/execute-one! datasource))))

(defn tasks-by-status [context args _parent]
  (let [datasource (-> context :db :datasource)
        status (:status args)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/where [:= :status status])
             (h/order-by [:created_at :desc])
             sql/format)
         (jdbc/execute! datasource))))

(defn create-task [context args _parent]
  (let [datasource (-> context :db :datasource)
        {:keys [title status]} args]
    (->> (-> (h/insert-into :task)
             (h/values [{:title title :status status}])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! datasource))))

(defn update-task [context args _parent]
  (let [datasource (-> context :db :datasource)
        {:keys [task_id title status]} args
        updates (cond-> {}
                  title (assoc :title title)
                  status (assoc :status status))]
    (->> (-> (h/update :task)
             (h/set updates)
             (h/where [:= :task_id [:cast task_id :uuid]])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! datasource))))

(defn delete-task [context args _parent]
  (let [datasource (-> context :db :datasource)
        task-id (:task_id args)]
    (->> (-> (h/delete-from :task)
             (h/where [:= :task_id [:cast task-id :uuid]])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! datasource))))

(def resolvers
  {:query/tasks tasks
   :query/task task
   :query/tasks-by-status tasks-by-status
   :mutation/create-task create-task
   :mutation/update-task update-task
   :mutation/delete-task delete-task})
