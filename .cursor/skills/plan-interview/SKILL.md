---
name: plan-interview
description: Interview the user one question at a time to reach shared understanding of a plan or design before implementation. Walk the design tree branch-by-branch, resolve decision dependencies, recommend answers, look up codebase facts autonomously, and wait for user decisions. Use when planning features, designs, refactors, or when the user wants to align on a plan before coding.
disable-model-invocation: true
---

# Plan Interview

Reach **shared understanding** with the user before changing anything. Interview relentlessly; implement nothing until the user explicitly confirms alignment.

## When to Use

- The user invokes `/plan-interview` or asks to align on a plan before implementation.
- A task is ambiguous, architectural, or has meaningful trade-offs.
- The user wants to walk through a design tree and resolve dependencies before acting.

## Core Rules

1. **One question at a time.** Ask a single question, then stop and wait for the user's answer. Never batch multiple questions in one message — that is bewildering.
2. **Facts vs decisions.**
   - **Facts** (discoverable in the codebase, docs, config, tests, or repo history): look them up yourself. Do not ask the user what you can verify.
   - **Decisions** (preferences, priorities, trade-offs, scope, acceptance criteria): present each to the user and wait for their answer.
3. **Recommend an answer** with every decision question. State your recommendation clearly, explain the trade-offs briefly, then ask for the user's choice.
4. **Do not enact the plan** — no code changes, file edits, commits, or implementation steps — until the user confirms you have reached shared understanding.
5. **Walk the design tree.** Decompose the plan into a decision tree. Resolve parent decisions before children. Track open branches and revisit unresolved dependencies.

## Workflow

### Phase 1 — Orient

1. Restate the goal in one or two sentences.
2. Explore the codebase and relevant docs to gather **facts** (existing patterns, constraints, related modules, prior art in the repo).
3. Sketch a mental **design tree**: major branches (scope, architecture, data model, APIs, UX, testing, rollout, etc.) and likely dependencies between them.
4. Tell the user what you learned from exploration and which branches you plan to walk through. Do not start deciding yet — set the map.

### Phase 2 — Interview (repeat until the tree is resolved)

For each decision point, in dependency order:

1. **Context** — One sentence on why this decision matters and what it unblocks downstream.
2. **Facts** — Summarize any codebase findings relevant to this choice (only if not already shared).
3. **Options** — Present the realistic options (usually 2–4). Keep it scannable.
4. **Recommendation** — State your recommended option and why.
5. **Question** — Ask exactly **one** clear question. End the message there.

**Question format template:**

```markdown
### [Short decision title]

**Context:** …

**What I found in the codebase:** … (omit if nothing new)

**Options:**
- **A.** …
- **B.** …
- **C.** … (if needed)

**My recommendation:** **B** — …

**Your call:** Which option do you want — A, B, or C (or something else)?
```

After the user answers:

- Record the decision.
- Mark the branch resolved or note follow-ups.
- Move to the next highest-priority **unblocked** decision.
- If the answer changes earlier assumptions, revisit affected branches explicitly.

### Phase 3 — Confirm shared understanding

When all branches are resolved, produce a **Decision Summary**:

```markdown
## Decision Summary

| # | Decision | Choice |
|---|----------|--------|
| 1 | … | … |

## Agreed plan (concise)
…

## Out of scope
…

## Open risks / follow-ups
…
```

Then ask exactly one closing question:

> **Do you confirm we have shared understanding and I should proceed with implementation?**

- If **no** or **not yet**: return to Phase 2 on the gaps they name.
- If **yes**: only then begin implementation (or hand off to the user's next instruction).

## Design Tree Guidance

Walk branches systematically. Typical top-level branches (adapt to the task):

1. **Goal & success criteria** — What does "done" mean?
2. **Scope** — In / out, MVP vs full, constraints.
3. **Architecture & placement** — Where does this live in the system?
4. **Interfaces & contracts** — APIs, events, data shapes.
5. **Behavior & edge cases** — Happy path and failure modes.
6. **Dependencies** — Libraries, services, migrations, feature flags.
7. **Testing & verification** — How will we know it works?
8. **Rollout & compatibility** — Breaking changes, migration, docs.

**Dependency rule:** Do not ask about implementation details that depend on an unresolved parent (e.g., don't ask about API response shape before agreeing on the resource model).

## What to Look Up (not ask)

Before and during the interview, proactively investigate:

- Existing modules, patterns, and naming conventions
- Related types, schemas, configs, and environment variables
- Tests and fixtures that define expected behavior
- Design docs, READMEs, ADRs, and comments in relevant files
- Git history or open PRs for recent related work

Summarize findings when they inform a decision; don't dump raw exploration logs.

## Anti-patterns

- Asking multiple questions in one message.
- Asking the user for facts available in the repo.
- Starting implementation before explicit confirmation.
- Re-asking settled decisions without reason.
- Presenting recommendations without trade-offs.
- Skipping the final confirmation step.

## Tone

Be direct and thorough, not apologetic. The interview should feel like pair-design: rigorous, respectful of the user's authority over decisions, and efficient with their attention.
