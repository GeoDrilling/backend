#pragma once
#include "stdint.h"
#include "NumProbes.h"

#define EXPORT_FAST_SIMULATION  
//! стартовая модель
//! @param[in] nprobes                                   - количество приборов
//! @param[in] num_probe[nprobes]                        - указатель на массив с номерами приборов
//! @param[in] npoints                                   - количество точек траектории
//! @param[in] md[npoints]                               - длина траектории
//! @param[in] tvd[npoints]                              - координаты z траектории (глубина, ось направлена вниз)
//! @param[in] x[npoints]                                - координаты x траектории (отход)
//! @param[in] zeni[npoints]                             - угол наклона прибора
//! @param[in] ro_by_phases[npoints, nprobes]            - кажущиеся сопротивления по фазам 
//! @param[in] ro_by_ampl[npoints, nprobes]              - кажущиеся сопротивления по амплитудам
//! @param[in, out] tvd_start                            - глубина границы пластов в точке x[0]
//! @param[in] min_tvd_start                             - нижняя граница подбора tvd_start
//! @param[in] max_tvd_start                             - верхняя граница подбора tvd_start
//! @param[in, out] alpha                                - угол наклона пласта
//! @param[in] min_alpha                                 - нижнаая граница подбора угла наклона пласта
//! @param[in] max_alpha                                 - верхняя граница подбора угла наклона пласта
//! @param[in, out] ro_up                                - сопротивление верхнего пласта
//! @param[out] min_ro_up                                - нижняя граница сопротивления верхнего пласта
//! @param[out] max_ro_up                                - верхняя граница сопротивления верхнего пласта
//! @param[int, out] kanisotropy_up                      - коэффициент анизотропии вехнего пласта
//! @param[out] min_kanisotropy_up                       - нижняя граница коэффициента анизотропии верхнего пласта
//! @param[out] max_kanisotropy_up                       - верхняя граница коэффициента анизотропии верхнего пласта
//! @param[in, out] ro_down                              - сопротивление нижнего пласта
//! @param[out] min_ro_down                              - нижняя граница сопротивления нижнего пласта
//! @param[out] max_ro_down                              - верхняя граница сопротивления нижнего пласта
//! @param[in, out] kanisotropy_down                     - коэффициент анизотропии нижнего пласта
//! @param[out] min_kanisotropy_down                     - нижняя граница коэффициента анизотропии нижнего пласта
//! @param[out] max_kanisotropy_down                     - верхняя граница коэффициента анизотропии нижнего пласта
//! @param[out] synt_ro_by_phases[npoints, nprobes]      - кажущиеся сопротивления по фазам, рассчитанные по подобранной модели
//! @param[out] synt_ro_by_ampl[npoints, nprobes]        - кажущиеся сопротивления по амплитудам, рассчитанные по подобранной модели
//! @param[out] misfit                                   - невязка
extern "C" int32_t EXPORT_FAST_SIMULATION StartModelSimpleHorizontalModel6Param1_5D_ByRo(
	const uint16_t nprobes, const uint32_t num_probe[],
	const uint16_t npoints, const double md[], const double tvd[], const double x[], const double zeni[],
	const double ro_by_phases[], const double ro_by_ampl[],
	double& tvd_start, const double min_tvd_start, const double max_tvd_start,
	double& alpha, const double min_alpha, const double max_alpha,
	double& ro_up, const double min_ro_up, const double max_ro_up,
	double& kanisotropy_up, const double min_kanisotropy_up, const double max_kanisotropy_up,
	double& ro_down, const double min_ro_down, const double max_ro_down,
	double& kanisotropy_down, const double min_kanisotropy_down, const double max_kanisotropy_down,
	double synt_ro_by_phases[], double synt_ro_by_ampl[], double& misfit
);

//! инверсия
//! @param[in] nprobes                                   - количество приборов
//! @param[in] num_probe[nprobes]                        - указатель на массив с номерами приборов
//! @param[in] npoints                                   - количество точек траектории
//! @param[in] md[npoints]                               - длина траектории
//! @param[in] tvd[npoints]                              - координаты z траектории (глубина, ось направлена вниз)
//! @param[in] x[npoints]                                - координаты x траектории (отход)
//! @param[in] zeni[npoints]                             - угол наклона прибора
//! @param[in] ro_by_phases[npoints, nprobes]            - кажущиеся сопротивления по фазам 
//! @param[in] ro_by_ampl[npoints, nprobes]              - кажущиеся сопротивления по амплитудам
//! @param[in, out] tvd_start                            - глубина границы пластов в точке x[0]
//! @param[in] min_tvd_start                             - нижняя граница подбора tvd_start
//! @param[in] max_tvd_start                             - верхняя граница подбора tvd_start
//! @param[in, out] alpha                                - угол наклона пласта
//! @param[in] min_alpha                                 - нижнаая граница подбора угла наклона пласта
//! @param[in] max_alpha                                 - верхняя граница подбора угла наклона пласта
//! @param[in, out] ro_up                                - сопротивление верхнего пласта
//! @param[out] min_ro_up                                - нижняя граница сопротивления верхнего пласта
//! @param[out] max_ro_up                                - верхняя граница сопротивления верхнего пласта
//! @param[in, out] kanisotropy_up                      - коэффициент анизотропии вехнего пласта
//! @param[out] min_kanisotropy_up                       - нижняя граница коэффициента анизотропии верхнего пласта
//! @param[out] max_kanisotropy_up                       - верхняя граница коэффициента анизотропии верхнего пласта
//! @param[in, out] ro_down                              - сопротивление нижнего пласта
//! @param[out] min_ro_down                              - нижняя граница сопротивления нижнего пласта
//! @param[out] max_ro_down                              - верхняя граница сопротивления нижнего пласта
//! @param[in, out] kanisotropy_down                     - коэффициент анизотропии нижнего пласта
//! @param[out] min_kanisotropy_down                     - нижняя граница коэффициента анизотропии нижнего пласта
//! @param[out] max_kanisotropy_down                     - верхняя граница коэффициента анизотропии нижнего пласта
//! @param[out] synt_ro_by_phases[npoints, nprobes]      - кажущиеся сопротивления по фазам, рассчитанные по подобранной модели
//! @param[out] synt_ro_by_ampl[npoints, nprobes]        - кажущиеся сопротивления по амплитудам, рассчитанные по подобранной модели
//! @param[out] misfit                                   - невязка
extern "C" int32_t EXPORT_FAST_SIMULATION SolverHorizontalModel6Param1_5DByRo(
	const uint16_t nprobes, const uint32_t num_probe[],
	const uint16_t npoints, const double md[], const double tvd[], const double x[], const double zeni[],
	const double ro_by_phase[], const double ro_by_ampl[],
	double& tvd_start, const double min_tvd_start, const double max_tvd_start,
	double& alpha, const double min_alpha, const double max_alpha,
	double& ro_up, const double min_ro_up, const double max_ro_up,
	double& kanisotropy_up, const double min_kanisotropy_up, const double max_kanisotropy_up,
	double& ro_down, const double min_ro_down, const double max_ro_down,
	double& kanisotropy_down, const double min_kanisotropy_down, const double max_kanisotropy_down,
	double synt_phases[], double synt_ampl[], double& misfit
);

//! расчёт сигналов
//! @param[in] nprobes                                   - количество приборов
//! @param[in] num_probe[nprobes]                        - указатель на массив с номерами приборов
//! @param[in] npoints                                   - количество точек траектории
//! @param[in] tvd[npoints]                              - координаты z траектории (глубина, ось направлена вниз)
//! @param[in] x[npoints]                                - координаты x траектории (отход)
//! @param[in] zeni[npoints]                             - угол наклона прибора
//! @param[in] tvd_start                                 - глубина границы пластов в точке x[0]
//! @param[in] alpha                                     - угол наклона пласта
//! @param[in] ro_up                                     - сопротивление верхнего пласта
//! @param[in] kanisotropy_up                            - коэффициент анизотропии вехнего пласта
//! @param[in, out] ro_down                              - сопротивление нижнего пласта
//! @param[in, out] kanisotropy_down                     - коэффициент анизотропии нижнего пласта
//! @param[out] synt_ro_by_phases[npoints, nprobes]      - кажущиеся сопротивления по фазам, рассчитанные по подобранной модели
//! @param[out] synt_ro_by_ampl[npoints, nprobes]        - кажущиеся сопротивления по амплитудам, рассчитанные по подобранной модели
extern "C" int32_t EXPORT_FAST_SIMULATION LoggingHorizontalModel6Param1_5DRo(
	const uint16_t nprobes, const uint32_t num_probe[],
	const uint16_t npoints, const double tvd[], const double x[], const double zeni[],
	const double tvd_start, const double alpha, const double ro_up, const double kanisotropy_up,
	const double ro_down, const double kanisotropy_down, double synt_ro_by_phases[], double synt_ro_by_ampl[]
);

//! TODO: интерфейс для построения эквивалентных областей
//! @param[in] nprobes       	                            - количество приборов
//! @param[in] num_probe[nprobes]                            - указатель на массив с номерами приборов
//! @param[in] npoints                                      - количество точек траектории
//! @param[in] tvd[npoints]                                 - координаты z траектории (глубина, ось направлена вниз)
//! @param[in] x[npoints]                                   - координаты x траектории (отход)
//! @param[in] zeni[npoints]                                - угол наклона прибора
//! @param[in] n_tvd_start                                  - количество значений на сетке для глубины границы пластов в точке x[0]
//! @param[in] tvd_start[n_tvd_start]                       - указатель на массив с сеткой для глубины границы пластов в точке x[0]
//! @param[in] n_alpha                                      - количество значений на сетке для угла наклона пласта
//! @param[in] alpha[n_alpha]                               - указатель на массив с сеткой для угла наклона пласта
//! @param[in] n_ro_up                                      - количество значений на сетке для сопротивления верхнего пласта
//! @param[in] ro_up[n_ro_up]                               - указатель на массив с сеткой для сопротивления верхнего пласта
//! @param[in] n_kanisotropy_up                             - количество значений на сетке для коэффициента анизотропии верхнего пласта
//! @param[in] kanisotropy_up[n_kanisotropy_up]             - указатель на массив с сеткой для коэффициента анизотропии верхнего пласта
//! @param[in] n_ro_down                                    - количество значений на сетке для сопротивления нижнего пласта
//! @param[in] ro_down[n_ro_down]                           - указатель на массив с сеткой для сопротивления нижнего пласта
//! @param[in] n_kanisotropy_down                           - количество значений на сетке для коэффициента анизотропии нижнего пласта
//! @param[in] kanisotropy_down[n_kanisotropy_down]         - указатель на массив с сеткой для коэффициента анизотропии нижнего пласта
//! @param[in] phases[nprobes]                              - указатель на массив экспериментальных разностей фаз
//! @param[in] ampls[nprobes]                               - указатель на массив экспериментальных амплитуд
//! @param[out] target_functions[]                          - целевые функции для указанных моделей
extern "C" int32_t EXPORT_FAST_SIMULATION TargetFunctions(
	const uint16_t nprobes, const uint32_t num_probe[],
	const uint16_t npoints, const double tvd[], const double x[], const double zeni[],
	const uint16_t n_tvd_start, const double tvd_start, 
	const uint16_t n_alpha, const double alpha,
	const uint16_t n_ro_up, const double ro_up,
	const uint16_t n_kanisotropy_up, const double kanisotropy_up,
	const uint16_t n_ro_down, const double ro_down,
	const uint16_t n_kanisotropy_down, const double kanisotropy_down,
	const double ro_by_phases[], const double ro_by_ampl[],
	double target_functions[]);
