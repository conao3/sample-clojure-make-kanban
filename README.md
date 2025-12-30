# sample-clojure-make-kanban

A sample Kanban board application built with Clojure and ClojureScript. This project demonstrates building a full-stack web application with a GraphQL API and reactive frontend.

## Overview

This repository contains a step-by-step tutorial for building a kanban board from scratch. The `sections` directory contains progressive stages of development:

- **section01** - Initial project setup and basic structure
- **section02** - Full-stack implementation with GraphQL API
- **section99** - Final production-ready version

## Tech Stack

### Backend

- [Clojure](https://clojure.org/) - Primary language
- [http-kit](https://github.com/http-kit/http-kit) - HTTP server
- [Reitit](https://github.com/metosin/reitit) - Routing
- [Lacinia](https://github.com/walmartlabs/lacinia) - GraphQL implementation
- [Component](https://github.com/stuartsierra/component) - Lifecycle management
- [next.jdbc](https://github.com/seancorfield/next-jdbc) + [HoneySQL](https://github.com/seancorfield/honeysql) - Database access
- [PostgreSQL](https://www.postgresql.org/) - Database

### Frontend

- [ClojureScript](https://clojurescript.org/) - Primary language
- [Reagent](https://reagent-project.github.io/) - React wrapper
- [Apollo Client](https://www.apollographql.com/docs/react/) - GraphQL client

## Requirements

- [Nix](https://nixos.org/) with flakes enabled (recommended)
- Or manually: JDK 21+, Clojure CLI, Node.js 22+, pnpm

## Getting Started

### Using Nix

```bash
nix develop
```

This provides all required development tools including GraalVM, Clojure, Node.js, and pnpm.

### Running the Application

Navigate to a section directory and start the REPL:

```bash
cd sections/section02
clj -M:dev:nrepl:repl
```

Start the system from the REPL:

```clojure
(require '[dev])
(dev/go)
```

### Running Tests

```bash
cd sections/section02
clj -M:dev:test
```

### Building for Production

```bash
cd sections/section02
clj -T:build uber
```

## Project Structure

```
sections/
  section01/           # Basic setup
  section02/           # Full implementation
    src/
      conao3/kanban/
        cljs/          # ClojureScript frontend
        handler.clj    # HTTP handlers
        router.clj     # Route definitions
        system.clj     # Component system
        resolver/      # GraphQL resolvers
    test/              # Test files
    resources/         # Static assets & config
  section99/           # Production version
```

## License

See LICENSE file for details.
