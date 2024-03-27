import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import io
from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
from flask import send_file
app = Flask(__name__)

def cMap(n, intensityValues, param1, param2, param1Name, param2Name):
    xValues = param1
    yValues = param2
    a = ''
    b = ''
    c = ''
    d = ''
    # Создание сетки для интерполяции
    X, Y = np.meshgrid(xValues, yValues)

    if param1Name == 'ro_up' or param1Name == 'ro_down':
        X = np.log10(X)
        a = 'log('
        b = ')'
    if param2Name == 'ro_up' or param2Name == 'ro_down':
        Y = np.log10(Y)
        c = 'log('
        d = ')'

    fig, ax = plt.subplots()

    # Создание цветовой карты
    cmap = ax.pcolormesh(X, Y, intensityValues, shading='auto', cmap='viridis', norm=colors.LogNorm())

    # Добавление изолиний
    CS = ax.contour(X, Y, intensityValues, colors='black')

    # Подписи к изолиниям
    ax.clabel(CS, inline=True, fontsize=8)

    # Добавление цветовой шкалы
    fig.colorbar(cmap, ax=ax)

    # Настройка осей
    ax.set_xlabel(a + param1Name + b)
    ax.set_ylabel(c + param2Name + d)

    # Вместо сохранения в файл, сохраняем в байтовый поток
    buf = io.BytesIO()
    plt.savefig(buf, format='png')
    plt.close(fig)
    buf.seek(0)  # Перемещаем указатель в начало потока
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

    img_buf = cMap(n, intensities, param1, param2, param1Name, param2Name)

    # Отправляем поток байтов как ответ
    return send_file(
        img_buf,
        as_attachment=True,
        download_name='color_map.png',
        mimetype='image/png'
    )

if __name__ == '__main__':
    app.run(debug=False)