(ns conao3.kanban.resolver.task
  (:require
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

(def TaskStatus
  [:map
   [:status :string]])

(def Context :map)

(def Parent :any)

(mx/defn task-id :- :string
  [task-id :- :any]
  (str "Task:" task-id))

(mx/defn tasks :- [:vector Task]
  [context :- Context
   _args :- :map
   _parent :- Parent]
  (let [db (-> context :db)]
    (->> (jdbc/execute! db
                        (-> (h/select :*)
                            (h/from :task)
                            (h/order-by [:created_at :desc])
                            sql/format))
         (mapv #(-> % (assoc :id (task-id (:task-id %))))))))

(mx/defn task :- [:maybe Task]
  [context :- Context
   args :- TaskId
   _parent :- Parent]
  (let [db (-> context :db)
        tid (:task-id args)
        res (jdbc/execute-one! db
                               (-> (h/select :*)
                                   (h/from :task)
                                   (h/where [:= :task_id [:cast tid :uuid]])
                                   sql/format))]
    (some-> res (assoc :id (task-id (:task-id res))))))

(mx/defn tasks-by-status :- [:vector Task]
  [context :- Context
   args :- TaskStatus
   _parent :- Parent]
  (let [db (-> context :db)
        status (:status args)]
    (->> (jdbc/execute! db
                        (-> (h/select :*)
                            (h/from :task)
                            (h/where [:= :status status])
                            (h/order-by [:created_at :desc])
                            sql/format))
         (mapv #(-> % (assoc :id (task-id (:task-id %))))))))

(mx/defn create-task :- Task
  [context :- Context
   args :- TaskInput
   _parent :- Parent]
  (let [db (-> context :db)
        {:keys [title status]} args
        res (jdbc/execute-one! db
                               (-> (h/insert-into :task)
                                   (h/values [{:title title :status status}])
                                   (h/returning :*)
                                   sql/format))]
    (-> res (assoc :id (task-id (:task-id res))))))

(mx/defn update-task :- Task
  [context :- Context
   args :- TaskUpdate
   _parent :- Parent]
  (let [db (-> context :db)
        {:keys [title status]} args
        tid (:task-id args)
        updates (cond-> {}
                  title (assoc :title title)
                  status (assoc :status status))
        res (jdbc/execute-one! db
                               (-> (h/update :task)
                                   (h/set updates)
                                   (h/where [:= :task_id [:cast tid :uuid]])
                                   (h/returning :*)
                                   sql/format))]
    (-> res (assoc :id (task-id (:task-id res))))))

(mx/defn delete-task :- Task
  [context :- Context
   args :- TaskId
   _parent :- Parent]
  (let [db (-> context :db)
        tid (:task-id args)
        res (jdbc/execute-one! db
                               (-> (h/delete-from :task)
                                   (h/where [:= :task_id [:cast tid :uuid]])
                                   (h/returning :*)
                                   sql/format))]
    (-> res (assoc :id (task-id (:task-id res))))))

(def resolvers
  {:Query/tasks tasks
   :Query/task task
   :Query/tasksByStatus tasks-by-status
   :Mutation/createTask create-task
   :Mutation/updateTask update-task
   :Mutation/deleteTask delete-task})
