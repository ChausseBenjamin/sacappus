# Sac À-PP US

Pot-pourrit de tout les APP fait (plutôt subit) pendant le bac en Génie
Informatique à [USherbrooke][1].

## How this repo works

- Chaque APP est développé dans sa propre branche (dans son propre dossier)
  * Seulement les fichier qui servent directement à la résolution son importés
  * Des scripts programmes c'est ok.
  * Pas de `.env` ou de `png` générés qui pèsent une tonne (or worst: Vivado bullshit)
- la branche `core` ne doit pas contenir aucun APP, seulement le tooling utilisé
  pour build les APPs (build containers type-shit)
- `master` contient tout les APP complétés ainsi que le tooling pour build les rapports.
  - seulement moi peut merge dans master, mes partners d'APP travaillent dans
    les branches qu'ils ont accès

Endgoal workflow:
1. Nouvel app: je crée sa branche à partir de `core` qui est vide et ajoute mon
   parnter comme collaborateur s'il l'est pas déjà.
2. Je donne à mon partner les permission pour update la nouvelle branche (et
   celles qu'il crée s'il en veut on-the-side)
3. Si du nouveaux tooling doit être fait pour compiler le rapport:
   - je vais dans `core` et update le build-container
4. À la fin d'un APP, je merge la branch dans master (potentiellement je fais
   une release pour le projet)

Initial setup for me:
- restrict `master` to only me
- require my approval for merging into `core`
- restrict creating branches that match a certain regex to only me
  (so my partner can't create `s8-app3` while we do `s6-app1` and block
  future me)

[1]: https://gegi.usherbrooke.ca
