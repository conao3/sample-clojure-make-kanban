.PHONY: all
all: native

UNAME_OS := $(shell uname -s)

ifeq ($(UNAME_OS),Darwin)
GRAAL_BUILD_ARGS += -H:-CheckToolchain
endif

.PHONY: repl
repl:
	clj -M:dev:repl

.PHONY: update
update:
	clojure -Sdeps '{:deps {com.github.liquidz/antq {:mvn/version "RELEASE"}}}' -M -m antq.core --upgrade --force

.PHONY: test
test:
	clojure -M:dev:test

.PHONY: uber
uber: target/kanban-standalone.jar

target/kanban-standalone.jar:
	clojure -T:build uber

.PHONY: native
native: target/kanban

target/kanban: target/kanban-standalone.jar
	native-image -jar $< \
	--features=clj_easy.graal_build_time.InitClojureClasses \
	--verbose \
	--no-fallback \
	--install-exit-handlers \
	$(GRAAL_BUILD_ARGS) \
	$@

.PHONY: clean
clean:
	rm -rf target
