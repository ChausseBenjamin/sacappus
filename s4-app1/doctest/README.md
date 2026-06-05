# Plan de test

## Définitions/Abréviations

Btn
: Poussoirs sur la carte Zybo du FPGA (0 et 1 respectivement)


Swtch
: Poussoirs sur la carte thermométrique fournie par l'UDS (0 et 1 respectivement)

Prox
: Valeur approximative de proximité du capteur entre 0 (proche) et 12 (loin)

1-Hot
: Index de la DEL allumé sur l'afficheur configuré en One-Hot (0-7)

Par.
: État des DELs (`{DEL2,LD0}`) indicant la parité (1 désigne allumé ici)

## Liste des tests

| \#Test | Btns  | Swtch | Prox. | Par. | 1-Hot | 7Segments | Succès |
|--------|-------|-------|-------|------|-------|-----------|--------|
| 1      | {0,0} | {0,0} | 0     | 1    | 0     | "00"      | `[ ]` |
| 2      | {0,0} | {0,0} | 1     | 0    | 0     | "01"      | `[ ]` |
| 3      | {0,0} | {0,0} | 2     | 1    | 1     | "02"      | `[ ]` |
| 4      | {0,0} | {0,0} | 3     | 0    | 1     | "03"      | `[ ]` |
| 5      | {0,0} | {0,0} | 4     | 1    | 2     | "04"      | `[ ]` |
| 6      | {0,0} | {0,0} | 5     | 0    | 3     | "05"      | `[ ]` |
| 7      | {0,0} | {0,0} | 6     | 1    | 3     | "06"      | `[ ]` |
| 8      | {0,0} | {0,0} | 7     | 0    | 4     | "07"      | `[ ]` |
| 9      | {0,0} | {0,0} | 8     | 1    | 5     | "08"      | `[ ]` |
| 10     | {0,0} | {0,0} | 9     | 0    | 5     | "09"      | `[ ]` |
| 11     | {0,0} | {0,0} | 10    | 1    | 6     | "10"      | `[ ]` |
| 12     | {0,0} | {0,0} | 11    | 0    | 6     | "11"      | `[ ]` |
| 13     | {0,0} | {0,0} | 12    | 1    | 7     | "12"      | `[ ]` |
| 14     | {1,0} | {1,0} | 0     | 0    | 0     | "00"      | `[ ]` |
| 15     | {1,0} | {1,0} | 1     | 1    | 0     | "01"      | `[ ]` |
| 16     | {1,0} | {1,0} | 2     | 0    | 1     | "02"      | `[ ]` |
| 17     | {1,0} | {1,0} | 3     | 1    | 1     | "03"      | `[ ]` |
| 18     | {1,0} | {1,0} | 4     | 0    | 2     | "04"      | `[ ]` |
| 19     | {1,0} | {1,0} | 5     | 1    | 3     | "05"      | `[ ]` |
| 20     | {1,0} | {1,0} | 6     | 0    | 3     | "06"      | `[ ]` |
| 21     | {1,0} | {1,0} | 7     | 1    | 4     | "07"      | `[ ]` |
| 22     | {1,0} | {1,0} | 8     | 0    | 5     | "08"      | `[ ]` |
| 23     | {1,0} | {1,0} | 9     | 1    | 5     | "09"      | `[ ]` |
| 24     | {1,0} | {1,0} | 10    | 0    | 6     | "0A"      | `[ ]` |
| 25     | {1,0} | {1,0} | 11    | 1    | 6     | "0b"      | `[ ]` |
| 26     | {1,0} | {1,0} | 12    | 0    | 7     | "0C"      | `[ ]` |
| 27     | {0,1} | {0,0} | 0     | 1    | 0     | "-5"      | `[ ]` |
| 28     | {0,1} | {0,0} | 1     | 0    | 0     | "-4"      | `[ ]` |
| 29     | {0,1} | {0,0} | 2     | 1    | 1     | "-3"      | `[ ]` |
| 30     | {0,1} | {0,0} | 3     | 0    | 1     | "-2"      | `[ ]` |
| 31     | {0,1} | {0,0} | 4     | 1    | 2     | "-1"      | `[ ]` |
| 32     | {0,1} | {0,0} | 5     | 0    | 3     | "00"      | `[ ]` |
| 33     | {0,1} | {0,0} | 6     | 1    | 3     | "01"      | `[ ]` |
| 34     | {0,1} | {0,0} | 7     | 0    | 4     | "02"      | `[ ]` |
| 35     | {0,1} | {0,0} | 8     | 1    | 5     | "03"      | `[ ]` |
| 36     | {0,1} | {0,0} | 9     | 0    | 5     | "04"      | `[ ]` |
| 37     | {0,1} | {0,0} | 10    | 1    | 6     | "05"      | `[ ]` |
| 38     | {0,1} | {0,0} | 11    | 0    | 6     | "06"      | `[ ]` |
| 39     | {0,1} | {0,0} | 12    | 1    | 7     | "07"      | `[ ]` |
| 40     | {1,1} | {0,0} | 0     | 1    | 0     | "Er"      | `[ ]` |
| 41     | {0,0} | {0,1} | 0     | 1    | 0     | "Er"      | `[ ]` |

Les tests 1-13 ont 3 l'utilités de:

- S'assurer du bon fonctionnement de l'afficheur 7-segment de valeurs
  décimales non-signées (aucun Btn pesé)
- S'assurer du bon fonctionnement des DELs en mode parité impaire
- S'assurer de bon fonctionnement des DELs s'allumant en mode One-Hot

Les tests 14-26 eux servent à:

- S'assurer du bon fonctionnement des DELs en mode parité paire (Swtch[1] pesé)
- S'assurer du bon fonctionnement de l'afficheur 7-segment de valeurs
  hexadécimales (Btn[0] pesé)

En ce qui concerne les tests 27-39, ils servent à:

- S'assurer du bon fonctionnement de l'afficheur 7-segment de valeurs
  signées (Btn[0] pesé)
- S'assurer du bon fonctionnement du module `Moins_5`

Les tests 40-41 s'assurent que l'afficheur 7-Segment affiche bel et bien
une erreur si:

- Swtch[1] est pesé
- Btn 0 et 1 sont pesés simultanément

Toute autre permutation de combinason de test semble superflue compte tenu de
la couverture des tests actuels.
