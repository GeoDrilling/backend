#include "PicassoLWD.FastSimulation.Native.h"
#include "ErrorCodes.h"
#include <vector>
#include <algorithm>

namespace {

	const std::vector<uint32_t> small_vikpb_probes = {
		LWD_LOOCH_VIKPB_ROL, LWD_LOOCH_VIKPB_ROH,
	};

	bool IsCorrectProbesNum(const uint16_t nprobes, const uint32_t* num_probe,
		const std::vector <std::vector<uint32_t>>& supported_probes) {
		if (nprobes < 1) return false;
		for (auto& v : supported_probes) {
			if (std::find(v.begin(), v.end(), num_probe[0]) != v.end()) {
				for (int i = 1; i < nprobes; ++i) {
					auto t = std::find(v.begin(), v.end(), num_probe[i]) == v.end();
					if (std::find(v.begin(), v.end(), num_probe[i]) == v.end()) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}

int32_t StartModelSimpleHorizontalModel6Param1_5D_ByRo(const uint16_t nprobes, const uint32_t num_probe[], const uint16_t npoints, const double md[], const double tvd[], const double x[], const double zeni[], const double ro_by_phases[], const double ro_by_ampl[], double& tvd_start, const double min_tvd_start, const double max_tvd_start, double& alpha, const double min_alpha, const double max_alpha, double& ro_up, const double min_ro_up, const double max_ro_up, double& kanisotropy_up, const double min_kanisotropy_up, const double max_kanisotropy_up, double& ro_down, const double min_ro_down, const double max_ro_down, double& kanisotropy_down, const double min_kanisotropy_down, const double max_kanisotropy_down, double synt_ro_by_phases[], double synt_ro_by_ampl[], double& misfit)
{
	if (!IsCorrectProbesNum(1, num_probe, {
		small_vikpb_probes
		})) {
		return E_ERROR_PROBE_UNKNOWN;
	}

	return E_ERROR_NO;
}

int32_t SolverHorizontalModel6Param1_5DByRo(const uint16_t nprobes, const uint32_t num_probe[], const uint16_t npoints, const double md[], const double tvd[], const double x[], const double zeni[], const double ro_by_phase[], const double ro_by_ampl[], double& tvd_start, const double min_tvd_start, const double max_tvd_start, double& alpha, const double min_alpha, const double max_alpha, double& ro_up, const double min_ro_up, const double max_ro_up, double& kanisotropy_up, const double min_kanisotropy_up, const double max_kanisotropy_up, double& ro_down, const double min_ro_down, const double max_ro_down, double& kanisotropy_down, const double min_kanisotropy_down, const double max_kanisotropy_down, double synt_phases[], double synt_ampl[], double& misfit)
{
	if (!IsCorrectProbesNum(1, num_probe, {
		small_vikpb_probes
		})) {
		return E_ERROR_PROBE_UNKNOWN;
	}

	return E_ERROR_NO;
}

int32_t LoggingHorizontalModel6Param1_5DRo(const uint16_t nprobes, const uint32_t num_probe[], const uint16_t npoints, const double tvd[], const double x[], const double zeni[], const double tvd_start, const double alpha, const double ro_up, const double kanisotropy_up, const double ro_down, const double kanisotropy_down, double synt_ro_by_phases[], double synt_ro_by_ampl[])
{
	if (!IsCorrectProbesNum(1, num_probe, {
		small_vikpb_probes
		})) {
		return E_ERROR_PROBE_UNKNOWN;
	}

	return E_ERROR_NO;
}

int32_t TargetFunctions(const uint16_t nprobes, const uint32_t num_probe[], const uint16_t npoints, const double tvd[], const double x[], const double zeni[], const uint16_t n_tvd_start, const double tvd_start, const uint16_t n_alpha, const double alpha, const uint16_t n_ro_up, const double ro_up, const uint16_t n_kanisotropy_up, const double kanisotropy_up, const uint16_t n_ro_down, const double ro_down, const uint16_t n_kanisotropy_down, const double kanisotropy_down, const double ro_by_phases[], const double ro_by_ampl[], double target_functions[])
{
	if (!IsCorrectProbesNum(1, num_probe, {
		small_vikpb_probes
		})) {
		return E_ERROR_PROBE_UNKNOWN;
	}
	
	return E_ERROR_NO;
}
