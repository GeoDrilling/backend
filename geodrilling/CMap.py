import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.colors as colors
from matplotlib.ticker import LogFormatterExponent, LogLocator
import io
from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
from flask import send_file
from scipy.interpolate import griddata
app = Flask(__name__)
name = {'ro_up': 'УЭС верхнего пласта', 'ro_down': 'УЭС нижнего пласта', 'kanisotropy_down': 'анизотропия нижнего пласта', 'kanisotropy_up': 'анизотропия верхнего пласта', 'alpha': 'угол падения', 'tvd_start': 'граница пласта'}

def interpolate_data(X, Y, intensities, n):
    xi = np.linspace(X.min(), X.max(), n)
    yi = np.linspace(Y.min(), Y.max(), n)
    xi, yi = np.meshgrid(xi, yi)
    
    zi = griddata((X.flatten(), Y.flatten()), intensities.flatten(), (xi, yi), method='cubic')
    if zi.min() < 0:
        zi = griddata((X.flatten(), Y.flatten()), intensities.flatten(), (xi, yi), method='linear')
    return xi, yi, zi

def cMap(n, intensityValues, param1, param2, param1Name, param2Name, vmin, vmax, contourLevels):
    xValues = param1
    yValues = param2
    X, Y = np.meshgrid(xValues, yValues)
    print(X)
    if param1Name == 'ro_up' or param1Name == 'ro_down':
        X = np.log10(X)

    if param2Name == 'ro_up' or param2Name == 'ro_down':
        Y = np.log10(Y)

    X, Y, intensityValues = interpolate_data(X, Y, intensityValues, 1000)
    fig, ax = plt.subplots()
    #print(intensityValues.min())
    #print(intensityValues.max())
    if vmin == -1:
        vmin = intensityValues.min()  
    if vmax == -1:
        vmax = intensityValues.max()  
    if contourLevels == [-1]:
        contourLevels = 10  

    cmap = ax.pcolormesh(X, Y, intensityValues, shading='auto', cmap='viridis', 
                         norm=colors.LogNorm(vmin=vmin, vmax=vmax) if vmin and vmax else colors.LogNorm())

    CS = ax.contour(X, Y, intensityValues, colors='black', levels=contourLevels if isinstance(contourLevels, list) else 10)

    ax.clabel(CS, inline=True, fontsize=8)
    fig.colorbar(cmap, ax=ax)

    ax.set_xlabel(name[param1Name])
    ax.set_ylabel(name[param2Name])

    buf = io.BytesIO()
    plt.savefig(buf, format='png')
    plt.close(fig)
    buf.seek(0)
    return buf

@app.route('/getColorMap', methods=['POST'])
def save_color_map():
    data = request.get_json()
    n = data['n']
    intensities = np.array(data['intensities']).reshape((n, n))
    param1 = data['param1']
    param2 = data['param2']
    param1Name = data['param1Name']
    param2Name = data['param2Name']
    vmin = data.get('colorMin')
    vmax = data.get('colorMax')
    contourLevels = data.get('level')

    img_buf = cMap(n, intensities, param1, param2, param1Name, param2Name, vmin, vmax, contourLevels)

    return send_file(
        img_buf,
        as_attachment=True,
        download_name='color_map.png',
        mimetype='image/png'
    )

if __name__ == '__main__':
    app.run(debug=False)