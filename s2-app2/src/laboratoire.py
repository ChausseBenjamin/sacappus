#!/usr/bin/python

import numpy as np
from matplotlib import pyplot as plt

# General parameters
RESOLUTION = 10000
num_k = 20

# FUNCTION
f_a = {"res": RESOLUTION, "freq": 1000}
f_a["t"] = np.linspace(0, 1 / f_a["freq"], RESOLUTION)
# 0-12V, 49.5% duty cycle square wave
f_a["y"] = np.concatenate(
    (
        np.ones(int(0.495 * RESOLUTION)) * 12,  # 49.5% on time
        np.zeros(int((0.505 * RESOLUTION))),  # 50.5% off time
    )
)
print(RESOLUTION * 0.495)
print(RESOLUTION * 0.505)

f_b = {"res": RESOLUTION, "freq": 5000}
f_b["t"] = np.linspace(0, 1 / f_b["freq"], f_b["res"])
# Redressement double alternance of 12V sine
f_b["y"] = np.abs(np.sin(2 * np.pi * f_b["freq"] * f_b["t"])) * 12


def genFourrier(f, num_k):
    """
    Takes a function stored in the f dictionary and calculates its fourrier transform.
    Appends all the necessary data to the f dictionary.
    :param f: function dictionary containing the following:
        - f["t"]: time array (x axis of the function but t is used for time)
        - f["y"]: amplitude array (y axis of the function) - must be the same length as f["t"]
    :param num_k: number of harmonics to calculate (higher = more precise but slower)
    """
    f["res"] = len(f["t"])

    # Deduce info about the function (knowing we only receive one period)
    T = f["t"][-1] - f["t"][0]
    w_0 = (2 * np.pi) / T
    dt = T / f["res"]
    print(f"T={T}\nw_0={w_0}\ndt={dt}")

    # We want to calculate the inside of the integral for all K harmonics
    # We will therefore need NUM_K arrays which are all of length f["res"]
    # Each cell must be able to contain a complex number
    u = np.zeros((num_k + 1, f["res"]), dtype=complex)
    for k in range(0, num_k + 1):
        u[k] = f["y"] * np.exp(-1j * k * w_0 * f["t"]) * dt

    # The inside of the integral needs to be summed and multiplied by (1/T) to obtain X(k)
    # Let's create that array (capital X)
    f["X"] = np.zeros(num_k + 1, dtype=complex)
    for k in range(0, num_k + 1):
        f["X"][k] = np.sum(u[k]) * (1 / T)

    # We can now plot every harmonic amplitude as stems as follows
    f["stem_k"] = range(-num_k, num_k + 1)
    f["amps"] = np.concatenate((np.flip(np.abs(f["X"][1:])), np.abs(f["X"])))

    # When it comes to the phase stem,
    # some angles are very close to zero but not quite, let's fix that
    f["X"].real[abs(f["X"].real) < 1e-15] = 0
    f["X"].imag[abs(f["X"].imag) < 1e-15] = 0
    ang = np.angle(f["X"])

    f["phases"] = np.concatenate((np.flip(-ang[1:]), ang))

    # Let's store every harmonic function as a list item in a 2d matrix
    f["cos_list"] = np.zeros((num_k + 1, f["res"]), dtype=float)

    # Let's try to rebuild the original signal (only the first harmonic now)
    f["cos_list"][0] = np.ones_like(f["t"]) * np.real(f["X"][0])
    f["cos_sum"] = f["cos_list"][0]

    # Store every harmonic separately
    for i in range(1, num_k + 1):
        f["cos_list"][i] += (
            2 * np.abs(f["X"][i]) * np.cos(i * w_0 * f["t"] + np.angle(f["X"][i]))
        )
        f["cos_sum"] += f["cos_list"][i]
    # Store the sum of all harmonics

    # Let's add key values back into the f dictionary
    f["T"] = T  # Period
    f["w_0"] = w_0  # Omega 0
    f["dt"] = dt  # with of dt in the integral approximation


genFourrier(f_a, num_k)
fig = plt.figure()
ax1 = fig.add_subplot(141)
# First plot is the original signal with the sum overlayed
ax1.plot(f_a["t"] * 1000, f_a["cos_sum"], color="orange")
ax1.plot(f_a["t"] * 1000, f_a["y"], color="blue")
ax1.set_title("Signal A en fonction du temps")
ax1.set_xlabel("Temps (ms)")
ax1.set_ylabel("Amplitude (V)")
# Add a legend
ax1.legend(["Somme des harmoniques", "Signal original"])
# Second plot shows every harmonic separately
ax2 = fig.add_subplot(142)
for i in range(1, num_k + 1):
    ax2.plot(f_a["t"] * 1000, f_a["cos_list"][i])
ax2.set_title("Harmoniques de A")
ax2.set_xlabel("Temps (ms)")
ax2.set_ylabel("Amplitude (V)")
# Third plot shows the amplitude of every harmonic
ax3 = fig.add_subplot(143)
ax3.stem(f_a["stem_k"], f_a["amps"])
ax3.set_title("Amplitude des harmoniques de A")
ax3.set_xlabel("Harmonique (k)")
ax3.set_ylabel("Amplitude (V)")
# Fourth plot shows the phase of every harmonic
ax4 = fig.add_subplot(144)
ax4.stem(f_a["stem_k"], f_a["phases"])
ax4.set_title("Phase des harmoniques de A")
ax4.set_xlabel("Harmonique (k)")
ax4.set_ylabel("Phase (rad)")

plt.show()
