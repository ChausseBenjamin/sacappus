"""
Fichiers d'exemples pour la problématique de l'APP6 (S2)
(c) Julien Rossignol & JB Michaud Université de Sherbrooke
v 1.1 Hiver 2024

"""

import sys
from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
import scipy.signal as signal

# Ensure presentation/ is on sys.path so 'from lib import helpers' works
sys.path.insert(0, str(Path(__file__).resolve().parent.parent))
from lib import helpers as hp


def butterworth(freq, type):
    # Type 'low' pour low pass
    # Type 'high'

    # Frequence de coupure des filtres, a ajuster

    wc = 2 * np.pi * freq  # fréquence de coupure rad/s

    # Generez les filtres butterworth avec la fonction signal.butter, sous quel forme est la sortie (polynome ou pole-zero-gain)?
    # Transformer la sortie de signal.butter dans l'autre representation (vous pouvez vous inspirer de la question A du probleme 1)
    b1, a1 = signal.butter(2, wc, type, analog=True)

    # Affichage des polynomes
    # print(f'Passe-{type} Frequence : {freq} Numérateur {b1}, Dénominateur {a1}')  # affiche les coefficients correspondants au filtre
    return b1, a1

def graph_bode(b1, a1):

    # Ajouter le code pour generer la carte des poles et zeros et le lieu de Bode
    # utiliser les fonctions dans helpers.py
    # hp.bodeplot(b1, a1, title)

    hp.bodeplot(b1, a1, "Délai de groupe")


def graph_delay_bode(b1, a1):
    magp, php, wp, fig, ax = hp.bodeplot(b1, a1, "Délai de groupe")
    hp.grpdel1(wp, -np.diff(php) / np.diff(wp), "Délai de groupe")

def graphe_poles_zeros(b1, a1, title):
    z1, p1, k1 = signal.tf2zpk(b1, a1)
    hp.pzmap1(z1, p1, title)

def graph_delai(b1, a1):
    magp, php, wp, fig, ax = hp.bodeplot(b1, a1, "Égalisateur")
    hp.grpdel1(wp, -np.diff(php) / np.diff(wp), "Égalisateur")

def series_fct(freq1, type1, freq2, type2):
    # type 'low' ou 'high'

    b1, a1 = butterworth(freq1, type1)
    z1, p1, k1 = signal.tf2zpk(b1, a1)

    b2, a2 = butterworth(freq2, type2)
    z2, p2, k2 = signal.tf2zpk(b2, a2)

    zt, pt, kt = hp.seriestf(z1, p1, k1, z2, p2, k2)

    return zt, pt, kt


def verif_k1_k2():
    k2 = 47.5/60.4
    # Passe bande
    zp, pp, kp = series_fct(1000, 'high', 5000, 'low')
    # Passe Bas
    ab, bb = butterworth(700, 'low')
    zb, pb, kb = signal.tf2zpk(ab, bb)
    # Passe haut
    ah, bh = butterworth(7000, 'high')
    zh, ph, kh = signal.tf2zpk(ah, bh)
    # Combinaison du passe haut et du passe bas
    zz, pz, kz = hp.paratf(zb, pb, -kb, zh, ph, -kh)
    # Combinaison de la combinaison precedente avec ke pass bande
    zt, pt, kt = hp.paratf(zz, pz, kz, zp, pp, kp*k2)
    # Fonction de transfert du circuit complet
    at, bt = signal.zpk2tf(zt, pt, kt)

    graph_bode(at, bt)

def prob1():
    az, bz = butterworth(7000, 'high')
    graph_bode(az, bz)

def prob1_2():
    az, bz = butterworth(7000, 'high')
    graphe_poles_zeros(az,bz, "du filtre erronné corrigé")

def prob2():
    az, bz = butterworth(7000, 'high')
    graph_delay_bode(az, bz)


def main():

    # prob1()
    # verif_k1_k2()
    prob1_2()
    # Fonction qui en print 2

    # prob2()

    plt.show()



#####################################
# Manière standardisée d'exécuter ce que vous désirez lorsque vous "exécutez" le fichier
# permet d'importer les fonctions définies ici comme un module dans un autre fichier .py
# voir par exemple le fichier helper.py qui n'exécutera jamais rien tout seul
# et dont les fonctions sont importées ici à titre de support pour ce qu'on essaie de faire
# pour l'instant ne rien modifier ci-dessous
if __name__ == '__main__':
    main()
