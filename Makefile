default: run

help:			## list out commands with descriptions
	@sed -ne '/@sed/!s/## //p' $(MAKEFILE_LIST)

run:			## generate karabiner.json (using kotlin)
	./gradlew run

ts:				## generate karabiner.json (using typescript)
	ts-node --compiler-options '{"module":"commonjs","target":"es2017"}' src/ts/rules.ts

ktfmt:          ## ktfmt changed files on this branch
	@echo "--- This script will run ktfmt on all changed files"
	@MERGE_BASE=$$(git merge-base HEAD origin/master); \
	MODIFIED_FILES=$$(git diff $$MERGE_BASE --diff-filter=ACMR --name-only --relative -- '*.kt'); \
	for FILE in $$MODIFIED_FILES; do \
		echo "Formatting $$FILE"; \
		ktfmt -F "$$FILE"; \
	done

ktfmt-all:
	@echo "--- This script will run ktfmt on all files"
	@ktfmt -F $(shell find . -name '*.kt')
