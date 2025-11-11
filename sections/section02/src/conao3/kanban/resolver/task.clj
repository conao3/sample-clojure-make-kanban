(ns conao3.kanban.resolver.task
  (:require
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [malli.experimental :as mx]
   [next.jdbc :as jdbc]))

(def Task
  [:map
   [:id :string]
   [:task-id :uuid]
   [:title :string]
   [:status :string]
   [:created-at inst?]
   [:updated-at inst?]])

(def TaskInput
  [:map
   [:title :string]
   [:status :string]])

(def TaskUpdate
  [:map
   [:task-id :string]
   [:title {:optional true} :string]
   [:status {:optional true} :string]])

(def TaskId
  [:map
   [:task-id :string]])

(def Context :map)

(def Parent :any)

(mx/defn task-id :- :string
  [task-id :- [:or :uuid :string]]
  (str "Task:" task-id))

(mx/defn tasks :- [:sequential Task]
  [context :- Context
   _args :- :map
   _parent :- Parent]
  (let [db (-> context :db)]
    (->> (-> (h/select :*)
             (h/from :task)
             (h/order-by [:created_at :desc])
             sql/format)
         (jdbc/execute! db)
         (map #(-> % (assoc :id (task-id (:task-id %)))))
         (cske/transform-keys csk/->camelCase))))

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
         (jdbc/execute-one! db)
         (#(some-> % (assoc :id (task-id (:task-id %)))))
         (cske/transform-keys csk/->camelCase))))

(mx/defn update-task :- Task
  [context :- Context
   args :- TaskUpdate
   _parent :- Parent]
  (let [db (-> context :db)
        {:keys [title status]} args
        tid (:task-id args)
        updates (cond-> {}
                  title (assoc :title title)
                  status (assoc :status status))]
    (->> (-> (h/update :task)
             (h/set updates)
             (h/where [:= :task_id [:cast tid :uuid]])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! db)
         (#(some-> % (assoc :id (task-id (:task-id %)))))
         (cske/transform-keys csk/->camelCase))))

(mx/defn delete-task :- Task
  [context :- Context
   args :- TaskId
   _parent :- Parent]
  (let [db (-> context :db)
        tid (:task-id args)]
    (->> (-> (h/delete-from :task)
             (h/where [:= :task_id [:cast tid :uuid]])
             (h/returning :*)
             sql/format)
         (jdbc/execute-one! db)
         (#(some-> % (assoc :id (task-id (:task-id %)))))
         (cske/transform-keys csk/->camelCase))))

(def resolvers
  {:Query/tasks tasks
   :Mutation/createTask create-task
   :Mutation/updateTask update-task
   :Mutation/deleteTask delete-task})
