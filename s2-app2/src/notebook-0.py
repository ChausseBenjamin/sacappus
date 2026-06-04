import marimo

__generated_with = "0.23.8"
app = marimo.App(width="medium")


@app.cell
def _():
    import numpy as np
    from matplotlib import pyplot as plt

    return np, plt


@app.cell
def _():
    # General parameters
    RESOLUTION = int(100000)
    num_k = 20
    return RESOLUTION, num_k


@app.cell
def _(RESOLUTION, np):
    # FUNCTION A PARAMETERS
    f = {
        "res": RESOLUTION,
        "freq": 1
    }
    f["t"] = np.linspace(0,1/f["freq"],f["res"])
    # 0-12V, 25% Duty Cycle
    f["y"] = np.concatenate(( 
        np.ones( int(f["res"]/20) )*5, 
        np.ones( 19*int(f["res"]/20) )*0 
    ))
    return (f,)


@app.cell
def _(f, np):
    # Sinus -5,5 doublement redresse (optional)
    f["y"] = np.abs(np.sin(np.linspace(0,2*np.pi,f["res"])))
    return


@app.cell
def _(RESOLUTION, f, np):
    # Sinus avec un DC offset de 0.5 (all pos.) 
    # Sa fréquence est 1/3 du T mesuré dans le sample
    f["y"] = np.sin(6*np.pi*f["t"])/2+(np.ones(RESOLUTION)*0.5)
    return


@app.cell
def _(f, np):
    # Fonction vraiment pas cachere (optional)
    f["y"] = np.concatenate(( 
        np.ones( int(f["res"]/4) )*1, 
        np.linspace(-2,2,int(f["res"]/4)),
        np.ones( int(f["res"]/4) )*-1, 
        np.ones( int(f["res"]/4) )*3 
    ))
    return


@app.cell
def _(f, plt):
    # Here is what our function looks like
    _fig = plt.figure()
    ax1 = _fig.add_subplot(111)
    ax1.set_title('Signal A en fonction du temps')
    ax1.set_xlabel('Temps (ms)')
    ax1.set_ylabel('Amplitude (V)')
    ax1.plot(f['t'] * 1000, f['y'])
    return


@app.cell
def _(f, np):
    # Deduce info about the function (knowing we only receive one period)
    T = f["t"][-1] - f["t"][0]
    w_0 = (2*np.pi)/T
    dt = T/f["res"]
    print(f"T={T}\nw_0={w_0}\ndt={dt}")
    return T, dt, w_0


@app.cell
def _(dt, f, np, num_k, w_0):
    # We want to calculate the inside of the integral for all K harmonics
    # We will therefore need NUM_K arrays which are all of length f["res"]
    # Each cell must be able to contain a complex number
    u = np.zeros((num_k + 1, f['res']), dtype=complex)
    for _k in range(0, num_k + 1):
        u[_k] = f['y'] * np.exp(-1j * _k * w_0 * f['t']) * dt
    return (u,)


@app.cell
def _(T, f, np, num_k, u):
    # The inside of the integral needs to be summed and multiplied by (1/T) to obtain X(k)
    # Let's create that array (capital X)
    f['X'] = np.zeros(num_k + 1, dtype=complex)
    for _k in range(0, num_k + 1):
        f['X'][_k] = np.sum(u[_k]) * (1 / T)
    return


@app.cell
def _(f, np, num_k, plt):
    # We can now plot every harmonic amplitude as stems as follows
    f['stem_k'] = range(-num_k, num_k + 1)
    f['amps'] = np.concatenate((np.flip(np.abs(f['X'][1:])), np.abs(f['X'])))
    _fig = plt.figure()
    ax2 = _fig.add_subplot(111)
    ax2.set_title('Amplitude des harmoniques de A')
    ax2.set_xlabel('Harmonique (k)')
    ax2.set_ylabel('Amplitude (V)')
    ax2.stem(f['stem_k'], f['amps'])
    return


@app.cell
def _(f, np, plt):
    # We it comes to the phase stem, some angles are very close to zero but not quite, let's fix that
    f['X'].real[abs(f['X'].real) < 1e-15] = 0
    f['X'].imag[abs(f['X'].imag) < 1e-15] = 0
    ang = np.angle(f['X'])
    f['phases'] = np.concatenate((np.flip(-ang[1:]), ang))
    _fig = plt.figure()
    ax3 = _fig.add_subplot(111)
    ax3.set_title('Phase des harmoniques de A')
    ax3.set_xlabel('Harmonique (k)')
    ax3.set_ylabel('Phase (rad)')
    ax3.stem(f['stem_k'], f['phases'])
    return


@app.cell
def _(f, np, num_k, plt, w_0):
    # Let's store every harmonic function as a list item in a 2d matrix
    f['cos_list'] = np.zeros((num_k + 1, f['res']), dtype=float)
    f['cos_list'][0] = np.ones_like(f['t']) * np.real(f['X'][0])
    f['cos_sum'] = f['cos_list'][0]
    # Let's try to rebuild the original signal (only the first harmonic now)
    for i in range(1, num_k + 1):
        f['cos_list'][i] += 2 * np.abs(f['X'][i]) * np.cos(i * w_0 * f['t'] + np.angle(f['X'][i]))
        f['cos_sum'] += f['cos_list'][i]
    # Store every harmonic separately
    _fig = plt.figure()
    ax4 = _fig.add_subplot(111)
    ax4.plot(f['t'], f['cos_sum'])
    # Store the sum of all harmonics
    ax4.plot(f['t'], f['y'])
    return


@app.cell
def _(T, dt, f, w_0):
    # Let's add key values back into the f dictionary
    f["T"] = T     # Period
    f["w_0"] = w_0 # Omega 0
    f["dt"] = dt   # with of dt in the integral approximation
    return


@app.cell
def _(f, plt):
    max_amp = max(f['y']) - min(f['y'])
    f['norm_diff'] = abs(f['cos_sum'] - f['y']) / max_amp
    _fig = plt.figure()
    ax5 = _fig.add_subplot(111)
    ax5.plot(f['t'], f['norm_diff'])
    return


@app.cell
def _(f, np):
    np.mean(f["norm_diff"])
    return


if __name__ == "__main__":
    app.run()
