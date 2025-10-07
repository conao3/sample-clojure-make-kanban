# kanban

## Backend

### REPL

Run REPL via `make repl`.

Then, eval `(go)` to start dev server and switch to `dev` namespace.

```clojure
user> (go)
:reloading (conao3.kanban.middleware conao3.kanban.resolver conao3.kanban.routes conao3.kanban.handler conao3.kanban.system.handler conao3.kanban.system.server conao3.kanban.system conao3.kanban dev user)
INFO conao3.kanban.system.handler - Starting Handler...
INFO conao3.kanban.system.handler - Started Handler
INFO conao3.kanban.system.server - Starting Server...
INFO conao3.kanban.system.server - Started Server at port: 8090
:ok
dev>
```

Also in `dev` namespace, you can use `(go)` to load edited files and restart dev server.

```clojure
dev> (go)
INFO conao3.kanban.system.server - Stopping Server...
INFO conao3.kanban.system.server - Stopped Server
INFO conao3.kanban.system.handler - Stopping Handler...
INFO conao3.kanban.system.handler - Stopped Handler
:reloading ()
INFO conao3.kanban.system.handler - Starting Handler...
INFO conao3.kanban.system.handler - Started Handler
INFO conao3.kanban.system.server - Starting Server...
INFO conao3.kanban.system.server - Started Server at port: 8090
:ok
```

Current running system is binded to `system`.

```clojure
dev> system
{:handler {:handler #function[clojure.lang.AFunction/1]},
 :server
 {:handler {:handler #function[clojure.lang.AFunction/1]},
  :server
  #object[org.httpkit.server.HttpServer 0x17f1e7ad "org.httpkit.server.HttpServer@17f1e7ad"],
  :server-stop-fn #function[clojure.lang.AFunction/1]}}
dev> (-> system :server :server org.httpkit.server/server-port)
8090
```

Usualy you can use `(go)`, but you can use `(stop)` and `(start)` to temporary manual control server.

```clojure
dev> (stop)
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.server - Stopping Server...
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.server - Stopped Server
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.handler - Stopping Handler...
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.handler - Stopped Handler
:ok
dev> (start)
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.handler - Starting Handler...
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.handler - Started Handler
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.server - Starting Server...
[nREPL-session-72a66d96-e4fd-4f71-b786-4632b18c3442] INFO conao3.kanban.system.server - Started Server at port: 8090
:ok
```

### Emacs

- `C-c M-j (cider-jack-in-clj)`: Run REPL and connect it
- `C-c M-n r (cider-ns-refresh)`: Load edited files (and auto restart server configured. see `.dir-locals.el`)

## Frontend
