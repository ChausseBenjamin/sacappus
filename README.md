# Sac À-PP US

Pot-pourrit de tout les APP fait (plutôt subit) pendant le bac en Génie
Informatique à [USherbrooke][1].

## Usage

### Creating a Project

**Initialisation**:

```sh
# Start from the core branch which is mostly empty
git checkout core && git pull

app="s[x]-app[y]"
init_file="$app/README.md"
git checkout -b "$app"
mkdir "$app" && touch "$init_file"
git add "$init_file" && git commit -m "setup $app"
```

**Compiling LaTeX, KnitR, Markdown**:

Copy and adapt to your usecase the template for projects:
`./assets/container/project-compose.yml`

Temporary [LaTeX][2] files should magically never appear in the git history
(I did some hacks with volume mounts and hardlinks to to some weird shit...)

**Submitting/sealing**:

Just push the code and make a PR to merge onto master. This should only be done
once a Project is finished as the squashed commit to `master` is meant to
represent the final *release*.

### Updating the compiler and/or `core` repo

Branch off of `core` and make a PR with the desired changes (ex: new compilation
format like [Typst][3], LICENSE change, etc...).

To avoid project squash commits from constantly apply the new change to master,
merge the new change to master, and keep using `core` as a starting point for
new projects.


[1]: https://gegi.usherbrooke.ca
[2]: https://www.latex-project.org
[3]: https://typst.app
