(ns conao3.kanban.resolver.task
  (:require
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [malli.experimental :as mx]
   [next.jdbc :as jdbc]))

(def Task
  [:map
   [:task_id :uuid]
   [:title :string]
   [:status :string]
   [:created_at inst?]
   [:updated_at inst?]])

(def TaskInput
  [:map
   [:title :string]
   [:status :string]])

(def TaskUpdate
  [:map
   [:task_id :string]
   [:title {:optional true} :string]
   [:status {:optional true} :string]])

(def TaskId
  [:map
   [:task_id :string]])

(def TaskStatus
  [:map
   [:status :string]])

(def Context :map)

(def Parent :any)

(mx/defn tasks :- [:vector Task]
  [context :- Context
   _args :- :map
   _parent :- Parent]
  (let [db (-> context :db)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/order-by [:created_at :desc])
             sql/format)
         (jdbc/execute! db))))

(mx/defn task :- [:maybe Task]
  [context :- Context
   args :- TaskId
   _parent :- Parent]
  (let [db (-> context :db)
        task-id (:task_id args)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/where [:= :task_id [:cast task-id :uuid]])
             sql/format)
         (jdbc/execute-one! db))))

(mx/defn tasks-by-status :- [:vector Task]
  [context :- Context
   args :- TaskStatus
   _parent :- Parent]
  (let [db (-> context :db)
        status (:status args)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/where [:= :status status])
             (h/order-by [:created_at :desc])
             sql/format)
         (jdbc/execute! db))))

(mx/defn create-task :- Task
  [context :- Context
   args :- TaskInput
   _parent :- Parent]
  (let [db (-> context :db)
        {:keys [title status]} args]
    (->> (-> (h/insert-into :task)
             (h/values [{:title title :status status}])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! db))))

(mx/defn update-task :- Task
  [context :- Context
   args :- TaskUpdate
   _parent :- Parent]
  (let [db (-> context :db)
        {:keys [task_id title status]} args
        updates (cond-> {}
                  title (assoc :title title)
                  status (assoc :status status))]
    (->> (-> (h/update :task)
             (h/set updates)
             (h/where [:= :task_id [:cast task_id :uuid]])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! db))))

(mx/defn delete-task :- Task
  [context :- Context
   args :- TaskId
   _parent :- Parent]
  (let [db (-> context :db)
        task-id (:task_id args)]
    (->> (-> (h/delete-from :task)
             (h/where [:= :task_id [:cast task-id :uuid]])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! db))))

(def resolvers
  {:query/tasks tasks
   :query/task task
   :query/tasks-by-status tasks-by-status
   :mutation/create-task create-task
   :mutation/update-task update-task
   :mutation/delete-task delete-task})
