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

def cMap(n, intensityValues):
    xValues = np.logspace(-1, 3, n)
    yValues = np.logspace(-1, 3, n)

    # Создание сетки для интерполяции
    X, Y = np.meshgrid(xValues, yValues)

    # Логарифмическое масштабирование координат
    logX = np.log10(X)
    logY = np.log10(Y)

    fig, ax = plt.subplots()

    # Создание цветовой карты
    cmap = ax.pcolormesh(logX, logY, intensityValues, shading='auto', cmap='viridis', norm=colors.LogNorm())

    # Добавление изолиний
    CS = ax.contour(logX, logY, intensityValues, colors='black')

    # Подписи к изолиниям
    ax.clabel(CS, inline=True, fontsize=8)

    # Добавление цветовой шкалы
    fig.colorbar(cmap, ax=ax)

    # Настройка осей
    ax.set_xlabel('Log(X)')
    ax.set_ylabel('Log(Y)')
    ax.set_title('Цветовая карта с изолиниями')

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
    img_buf = cMap(n, intensities)

    # Отправляем поток байтов как ответ
    return send_file(
        img_buf,
        as_attachment=True,
        download_name='color_map.png',
        mimetype='image/png'
    )

if __name__ == '__main__':
    app.run(debug=False)