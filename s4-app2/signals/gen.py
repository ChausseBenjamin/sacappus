#!/bin/python
"""
28 January 2025
@author: Jacob Lefebvre
Fonctionnement:
Il suffit de créer une fonction retournant un list numpy dans la class Signal et l'appeler dans un string dans la dernière fonction (process_signals).
"""

import numpy as np
from matplotlib import pyplot as plt
import matplotlib
import random
from random import randint

# To avoid the error : AttributeError: 'FigureCanvasInterAgg' object has no attribute 'tostring_rgb' of matplotlib backend
matplotlib.use("TkAgg")


class Signals:
    def sinus(self, f, fs, ne):
        t = np.arange(ne)
        return 0.99 * np.sin(f * (2 * np.pi * t) / fs)

    def cosinus(self, f, fs, ne):
        t = np.arange(ne)
        return 0.99 * np.cos(f * (2 * np.pi * t) / fs)

    def abs_triangle(self, f, fs, ne):
        """
        Generate a triangle wave signal starting at 0, going to 1, then to -1, and back to 0 in one period.

        :param f: Frequency of the triangle wave.
        :param fs: Sampling rate.
        :param ne: Number of samples in the signal.
        :return: A triangle wave signal array.
        """
        t = np.arange(ne) / fs  # Time array in seconds
        period = 1 / f  # Period of the triangle wave
        signal = 4 * (t % period) / period  # Ramp from 0 to 4 within the period
        signal = np.where(
            signal <= 2, signal - 1, 3 - signal
        )  # Shape into a triangle wave
        return 0.99 * signal  # Scale to range between -0.99 and 0.99

    def triangle(self, f, fs, ne):
        t = np.arange(ne) / fs  # Time array in seconds
        period = 1 / f  # Period of the triangle wave
        signal = 4 * (t % period) / period  # Split the period into 4 parts
        signal = np.where(signal <= 1, signal, signal)  # 0 to p/4, start at 0 to 1
        signal = np.where(
            (signal > 1) & (signal <= 2), 2 - signal, signal
        )  # p/4 to p/2, start at 1 to 0
        signal = np.where(
            (signal > 2) & (signal <= 3), -(signal - 2), signal
        )  # p/2 to 3p/4, start at 0 to -1
        signal = np.where(
            signal > 3, -4 + signal, signal
        )  # 3p/4 to p, start at -1 to 0
        return 0.99 * signal  # Scale to range between -0.99 and 0.99

    def sawtooth(self, f, fs, ne):
        t = np.arange(ne)
        return 0.99 * (2 * (t * f / fs - np.floor(t * f / fs + 0.5)))

    def gaussian_pulse(self, f, fs, ne):
        """
        Generate a Gaussian pulse signal.

        :param f: Frequency of the pulse (used to calculate the time axis).
        :param fs: Sampling rate (used to scale the time axis).
        :param ne: Number of samples in the signal.
        :return: A Gaussian pulse signal array.
        """
        t = np.arange(ne) / fs  # Time axis scaled by the sampling rate
        width = 1.0 / f  # Width is inversely proportional to the frequency
        pulse = np.exp(-(((t - ne / (2 * fs)) / width) ** 2))  # Centered Gaussian
        return 0.99 * pulse / np.max(pulse)  # Normalize to 0.99

    def square(self, f, fs, ne):
        t = np.arange(ne)
        return 0.99 * np.sign(np.sin(f * (2 * np.pi * t) / fs))


Sig = Signals()


def twosCom_dec2Hex24(dec):
    if dec >= 0:
        return "{0:06X}".format(dec)
    else:
        bin1 = "{0:024b}".format(dec)
        bin_comp = "".join("1" if x == "0" else "0" for x in bin1[1:])
        bin_comp2 = "1" + "{0:023b}".format(int(bin_comp, 2) + 1)
        return "{0:06X}".format(int(bin_comp2, 2))


def conversion_hexa(y):
    y_int = [int(y_i * pow(2, 23)) for y_i in y]
    y_hex = [twosCom_dec2Hex24(y_i) for y_i in y_int]
    return y_hex


# Fonction d'ajout de bruit
def add_noise(y, noise_level):
    noisy_signal = np.copy(y)
    noise = np.random.uniform(-noise_level / 100, noise_level / 100, size=len(y))
    return noisy_signal + noise


# Fonction d'écriture dans le format pour VHDL
def vhdl_writing(y_hex, signal_type: str = "", f: int = 1000, noise: bool = False):
    # Write signal
    with open(
        f"{signal_type}SignalHexa_{f}Hz{'_Noise' if noise else ''}.txt", "a"
    ) as f_vhdl:
        for data in y_hex:
            f_vhdl.write(f"{data}\n")


def signal_creation(f, fs, ne, noise_level, signal_name, save: bool = False):
    """Create and process a signal. Use the signal_type to select the signal function."""
    sig = getattr(Sig, signal_name)(f, fs, ne)
    sig_hex = conversion_hexa(sig)

    sig_noise = add_noise(sig, noise_level)
    sig_noise_hex = conversion_hexa(sig_noise)

    # if True:
    if save:
        vhdl_writing(sig_hex, signal_name, f)
        vhdl_writing(sig_noise_hex, signal_name, f, noise=True)

    return sig, sig_hex, sig_noise, sig_noise_hex


def process_signals(
    f, fs, ne, noise_level, config: tuple[bool, bool] = (True, False), *args: str
):
    """Create and process signals.
    :param f: Frequency of the signal.
    :param fs: Sampling rate.
    :param ne: Number of samples in the signal.
    :param noise_level: Level of noise to add.
    :param config: Tuple of booleans (show_signals, save_signals).
    :param args: Signal names to process. just list the names of the signals you want to process. e.g. "sinus", "cosinus", "triangle", "square"
    """
    t = np.arange(ne)
    colors: list = [
        "black",
        "blue",
        "red",
        "green",
        "purple",
        "orange",
        "pink",
        "brown",
        "grey",
    ]
    signal_names = [arg for arg in args]
    signals = {}
    for signal_name in signal_names:
        try:
            signals[signal_name] = signal_creation(
                f, fs, ne, noise_level, signal_name, save=config[1]
            )
        except AttributeError:
            print(f"Signal {signal_name} not found.")

    if config[0]:
        fig, axs = plt.subplots(2, 1, figsize=(10, 8))
        fig.suptitle("Signals")

        axs[0].set_title("Noiseless Signals")
        axs[1].set_title("Noisy Signals")

        legend = []
        for signal_type, signal in signals.items():
            # ----- Plot noiseless signals ----- #
            axs[0].plot(
                t,
                signal[0],
                color=colors[list(signals.keys()).index(signal_type) % len(colors)],
            )
            # ----- Plot noisy signals ----- #
            axs[1].plot(
                t,
                signal[2],
                color=colors[list(signals.keys()).index(signal_type) % len(colors)],
            )
            # ----- Link color & signal in legend ----- #
            legend.append(signal_type)

        # ----- Add legend ----- #
        axs[0].legend(legend)
        axs[1].legend(legend)
        axs[0].grid()
        axs[1].grid()

        plt.show()


# Parameters
# freq = 1e3  # Fréquence en Hz
freq_ech = 48000  # Fréquence d'échantillonnage
n_ech = 192000  # 2s  # Nombre d'échantillons

# Ajout de bruit
noise_level = 4  # Niveau de bruit en pourcentage

# Création et affichage des signaux, les booléens sont pour afficher et sauvegarder les signaux, respectivement

for freq in [200, 500, 1000, 20000, 26000]:
    process_signals(
        freq,
        freq_ech,
        n_ech,
        noise_level,
        (False, True),
        "sinus",
        "cosinus",
        "triangle",
        "square",
    )
