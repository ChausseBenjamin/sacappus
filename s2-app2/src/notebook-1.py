import marimo

__generated_with = "0.23.8"
app = marimo.App()


@app.cell
def _():
    import numpy as np
    import matplotlib.pyplot as plt

    return np, plt


@app.cell
def _():
    # Lieu de bode du filtre passe bas avant le suiveur
    R=7000 # Ohm
    C=0.225e-6 # Farads
    return C, R


@app.cell
def _(C, R, np):
    RESOLUTION = int(1e6)
    max_Hertz = int(2e4)

    f = np.linspace(1,max_Hertz,RESOLUTION)
    A = abs(1/(1j*2*np.pi*f*R*C+1))
    Alog = 20*np.log10(A)
    return A, Alog, f


@app.cell
def _(A, f, plt):
    _fig = plt.figure()
    _ax1 = _fig.add_subplot(111)
    _ax1.set_title('Lieu de bode du filtre passe-bas en amont du suiveur')
    _ax1.set_ylabel('Amplitude (A)')
    _ax1.set_xlabel('Fréquence (Hz)')
    _ax1.semilogx(f, A)
    return


@app.cell
def _(Alog, f, plt):
    _fig = plt.figure()
    _ax1 = _fig.add_subplot(111)
    _ax1.semilogx(f, Alog)
    _ax1.set_title('Lieu de bode du filtre passe-bas en amont du suiveur')
    _ax1.set_xlabel('Fréquence (Hz)')
    _ax1.set_ylabel('Amplitude (dB)')
    return


@app.cell
def _(np):
    def find_nearest(array, value):
        array = np.asarray(array)
        idx = np.abs(array - value).argmin()
        return array[idx]

    return (find_nearest,)


@app.cell
def _(Alog, f, find_nearest):
    _index_15 = int(find_nearest(f, 15).round())
    print(f'Amortissement du filtre à 15Hz (dB): {Alog[_index_15]}')
    index_10k = int(find_nearest(f, 10000).round())
    print(f'Amortissement du filtre à 10kHz (dB): {Alog[index_10k]}')
    return


@app.cell
def _():
    # Filtre passe haut après le suiveur
    R_1 = 11000  # Ohm
    C_1 = 2e-06  # Farad
    return C_1, R_1


@app.cell
def _(C_1, R_1, f, np):
    A_1 = abs(R_1 / (1 / (1j * 2 * np.pi * f * C_1) + R_1))
    Alog_1 = 20 * np.log10(A_1)
    return A_1, Alog_1


@app.cell
def _(A_1, f, plt):
    _fig = plt.figure()
    _ax1 = _fig.add_subplot(111)
    _ax1.set_title('Lieu de bode du filtre passe-haut après le suiveur')
    _ax1.set_ylabel('Amplitude (A)')
    _ax1.set_xlabel('Fréquence (Hz)')
    _ax1.semilogx(f, A_1)
    return


@app.cell
def _(Alog_1, f, plt):
    _fig = plt.figure()
    _ax1 = _fig.add_subplot(111)
    _ax1.semilogx(f, Alog_1)
    _ax1.set_title('Lieu de bode du filtre passe-bas après suiveur')
    _ax1.set_xlabel('Fréquence (Hz)')
    _ax1.set_ylabel('Amplitude (dB)')
    return


@app.cell
def _(Alog_1, f, find_nearest):
    index_1 = int(find_nearest(f, 1).round())
    print(f'Amortissement du filtre à 1Hz (dB): {Alog_1[index_1]}')
    _index_15 = int(find_nearest(f, 15).round())
    print(f'Amortissement du filtre à 15Hz (dB): {Alog_1[_index_15]}')
    return


if __name__ == "__main__":
    app.run()
