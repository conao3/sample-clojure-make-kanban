(ns conao3.kanban.resolver.task-test
  (:require
   [clojure.test :as t]
   [com.stuartsierra.component :as component]
   [conao3.kanban.resolver.task :as c.resolver.task]
   [conao3.kanban.system :as c.system]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [next.jdbc :as jdbc]))

(def system nil)

(defn system-fixture [f]
  (alter-var-root #'system (constantly (-> (c.system/new-system :test) (dissoc :server))))
  (alter-var-root #'system component/start-system)
  (f)
  (alter-var-root #'system component/stop-system)
  (alter-var-root #'system (constantly nil)))

(defn clean-db-fixture [f]
  (let [db (-> system :db :db)]
    (jdbc/execute! db (sql/format (h/truncate :task :restart-identity)))
    (f)
    (jdbc/execute! db (sql/format (h/truncate :task :restart-identity)))))

(t/use-fixtures :once system-fixture)
(t/use-fixtures :each clean-db-fixture)

(defn- make-context []
  {:db (-> system :db :db)})

(t/deftest tasks-test
  (let [context (make-context)
        task1 (c.resolver.task/create-task context {:title "Task 1" :status "todo"} nil)
        task2 (c.resolver.task/create-task context {:title "Task 2" :status "done"} nil)
        result (c.resolver.task/tasks context {} nil)]
    (t/is (= 2 (count result)))
    (t/is (= "Task 2" (-> result first :title)))
    (t/is (= "Task 1" (-> result second :title)))))

(t/deftest task-test
  (let [context (make-context)
        created (c.resolver.task/create-task context {:title "Test Task" :status "todo"} nil)
        task-id (str (:task-id created))
        result (c.resolver.task/task context {:task-id task-id} nil)]
    (t/is (some? result))
    (t/is (= "Test Task" (:title result)))
    (t/is (= "todo" (:status result)))))

(t/deftest task-not-found-test
  (let [context (make-context)
        result (c.resolver.task/task context {:task-id "00000000-0000-0000-0000-000000000000"} nil)]
    (t/is (nil? result))))

(t/deftest tasks-by-status-test
  (let [context (make-context)]
    (c.resolver.task/create-task context {:title "Task 1" :status "todo"} nil)
    (c.resolver.task/create-task context {:title "Task 2" :status "done"} nil)
    (c.resolver.task/create-task context {:title "Task 3" :status "todo"} nil)
    (let [result (c.resolver.task/tasks-by-status context {:status "todo"} nil)]
      (t/is (= 2 (count result)))
      (t/is (every? #(= "todo" (:status %)) result)))))

(t/deftest create-task-test
  (let [context (make-context)
        result (c.resolver.task/create-task context {:title "New Task" :status "in-progress"} nil)]
    (t/is (some? result))
    (t/is (some? (:task-id result)))
    (t/is (= "New Task" (:title result)))
    (t/is (= "in-progress" (:status result)))
    (t/is (= (str "Task:" (:task-id result)) (:id result)))))

(t/deftest update-task-test
  (let [context (make-context)
        created (c.resolver.task/create-task context {:title "Original" :status "todo"} nil)
        task-id (str (:task-id created))
        result (c.resolver.task/update-task context {:task-id task-id :title "Updated" :status "done"} nil)]
    (t/is (some? result))
    (t/is (= "Updated" (:title result)))
    (t/is (= "done" (:status result)))))

(t/deftest update-task-partial-test
  (let [context (make-context)
        created (c.resolver.task/create-task context {:title "Original" :status "todo"} nil)
        task-id (str (:task-id created))
        result (c.resolver.task/update-task context {:task-id task-id :title "Updated"} nil)]
    (t/is (some? result))
    (t/is (= "Updated" (:title result)))
    (t/is (= "todo" (:status result)))))

(t/deftest delete-task-test
  (let [context (make-context)
        created (c.resolver.task/create-task context {:title "To Delete" :status "todo"} nil)
        task-id (str (:task-id created))
        result (c.resolver.task/delete-task context {:task-id task-id} nil)]
    (t/is (some? result))
    (t/is (= "To Delete" (:title result)))
    (let [tasks (c.resolver.task/tasks context {} nil)]
      (t/is (= 0 (count tasks))))))
