#include "ru_nsu_fit_geodrilling_services_lib_NativeLibrary.h"
#include "./GroupProject/PicassoLWD.FastSimulation.Native.h"
#include <iostream>
#include <cmath>

JNIEXPORT jobject JNICALL Java_ru_nsu_fit_geodrilling_services_lib_NativeLibrary_startModelSimpleHorizontalModel6Param1_15D_1ByRo
  (JNIEnv *env, jobject obj, jint nprobes, jintArray num_probe,
  jint npoints, jdoubleArray md, jdoubleArray tvd, jdoubleArray x, jdoubleArray zeni,
  jdoubleArray ro_by_phases, jdoubleArray ro_by_ampl, jdouble tvd_start,
  jdouble alpha,
  jdouble ro_up,
  jdouble kanisotropy_up,
  jdouble ro_down,
  jdouble kanisotropy_down) {
    jdoubleArray synt_ro_by_phasesJ = env->NewDoubleArray(nprobes * npoints);
    jdoubleArray synt_ro_by_amplJ = env->NewDoubleArray(nprobes * npoints);
    uint32_t* num_probeC = (uint32_t*) env->GetIntArrayElements(num_probe, NULL);
    jdouble* mdC = env->GetDoubleArrayElements(md, NULL);
    jdouble* tvdC = env->GetDoubleArrayElements(tvd, NULL);
    jdouble* xC = env->GetDoubleArrayElements(x, NULL);
    jdouble* zeniC = env->GetDoubleArrayElements(zeni, NULL);
    jdouble* ro_by_phasesC = env->GetDoubleArrayElements(ro_by_phases, NULL);
    jdouble* ro_by_amplC = env->GetDoubleArrayElements(ro_by_ampl, NULL);
    jdouble min_tvd_start = NAN;
    jdouble max_tvd_start = NAN;
    jdouble min_alpha = NAN;
    jdouble max_alpha = NAN;
    jdouble min_ro_up = NAN;
    jdouble max_ro_up = NAN;
    jdouble min_kanisotropy_up = NAN;
    jdouble max_kanisotropy_up = NAN;
    jdouble min_ro_down = NAN;
    jdouble max_ro_down = NAN;
    jdouble min_kanisotropy_down = NAN;
    jdouble max_kanisotropy_down = NAN;
    jdouble* synt_ro_by_phases = env->GetDoubleArrayElements(synt_ro_by_phasesJ, NULL);
    jdouble* synt_ro_by_ampl = env->GetDoubleArrayElements(synt_ro_by_amplJ, NULL);
    jdouble misfit = 0;

    jint status = StartModelSimpleHorizontalModel6Param1_5D_ByRo(nprobes, num_probeC, npoints, mdC, tvdC, xC, zeniC, ro_by_phasesC, ro_by_amplC,
  		tvd_start, min_tvd_start, max_tvd_start,
  		alpha, min_alpha, max_alpha,
  		ro_up, min_ro_up, max_ro_up,
  		kanisotropy_up, min_kanisotropy_up, max_kanisotropy_up,
  		ro_down, min_ro_down, max_ro_down,
  		kanisotropy_down, min_kanisotropy_down, max_kanisotropy_down,
  		synt_ro_by_phases, synt_ro_by_ampl,
  		misfit);

      env->ReleaseIntArrayElements(num_probe, (jint*) num_probeC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(md, mdC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(tvd, tvdC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(x, xC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(zeni, zeniC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(ro_by_phases, ro_by_phasesC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(ro_by_ampl, ro_by_amplC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(synt_ro_by_phasesJ, synt_ro_by_phases, 0);
      env->ReleaseDoubleArrayElements(synt_ro_by_amplJ, synt_ro_by_ampl, 0);

      jclass outputModelClass = env->FindClass("ru/nsu/fit/geodrilling/model/OutputModel");
      jmethodID constructorOutputModel = env->GetMethodID(outputModelClass, "<init>", "(DD[D[DDDDDDDDDDDDDDI)V");

      jobject outputModel = env->NewObject(outputModelClass, constructorOutputModel, min_kanisotropy_down, max_kanisotropy_down,
        synt_ro_by_phasesJ, synt_ro_by_amplJ, misfit, kanisotropy_down, 
        min_ro_down, max_ro_down, ro_down, kanisotropy_up,
        min_kanisotropy_up, max_kanisotropy_up, ro_up, min_ro_up,
        max_ro_up, alpha, tvd_start, status);

    return outputModel;
  }
JNIEXPORT jobject JNICALL Java_ru_nsu_fit_geodrilling_services_lib_NativeLibrary_SolverHorizontalModel6Param1_15DByRo
  (JNIEnv *env, jobject obj, jint nprobes, jintArray num_probe,
  jint npoints, jdoubleArray md, jdoubleArray tvd, jdoubleArray x, jdoubleArray zeni,
  jdoubleArray ro_by_phases, jdoubleArray ro_by_ampl, jdouble tvd_start, jdouble min_tvd_start, jdouble max_tvd_start,
  jdouble alpha, jdouble min_alpha, jdouble max_alpha,
  jdouble ro_up, jdouble min_ro_up, jdouble max_ro_up,
  jdouble kanisotropy_up, jdouble min_kanisotropy_up, jdouble max_kanisotropy_up,
  jdouble ro_down, jdouble min_ro_down, jdouble max_ro_down,
  jdouble kanisotropy_down, jdouble min_kanisotropy_down, jdouble max_kanisotropy_down
  ) {
    jdoubleArray synt_ro_by_phasesJ = env->NewDoubleArray(nprobes * npoints);
    jdoubleArray synt_ro_by_amplJ = env->NewDoubleArray(nprobes * npoints);
    
    uint32_t* num_probeC = (uint32_t*) env->GetIntArrayElements(num_probe, NULL);
    jdouble* mdC = env->GetDoubleArrayElements(md, NULL);
    jdouble* tvdC = env->GetDoubleArrayElements(tvd, NULL);
    jdouble* xC = env->GetDoubleArrayElements(x, NULL);
    jdouble* zeniC = env->GetDoubleArrayElements(zeni, NULL);
    jdouble* ro_by_phasesC = env->GetDoubleArrayElements(ro_by_phases, NULL);
    jdouble* ro_by_amplC = env->GetDoubleArrayElements(ro_by_ampl, NULL);

    jdouble* synt_ro_by_phases = env->GetDoubleArrayElements(synt_ro_by_phasesJ, NULL);
    jdouble* synt_ro_by_ampl = env->GetDoubleArrayElements(synt_ro_by_amplJ, NULL);
    jdouble misfit = 0;
/*
    jint status = SolverHorizontalModel6Param1_5DByRoResRo(
		nprobes, num_probeC, npoints, tvdC, tvdC, xC, zeniC,
		ro_by_phasesC, ro_by_amplsC,
		tvdstart, NAN, NAN,
		alpha, NAN, NAN,
		ro_up, NAN, NAN,
		kanisotropy_up, NAN, NAN,
		ro_down, NAN, NAN,
		kanisotrypy_down, NAN, NAN,
		synt_ro_by_phase, synt_ro_by_ampls, misfit
	);*/

    jint status = SolverHorizontalModel6Param1_5DByRoResRo(nprobes, num_probeC, npoints, mdC, tvdC, xC, zeniC, ro_by_phasesC, ro_by_amplC,
  		tvd_start, min_tvd_start, max_tvd_start,
  		alpha, min_alpha, max_alpha,
  		ro_up, min_ro_up, max_ro_up,
  		kanisotropy_up, min_kanisotropy_up, max_kanisotropy_up,
  		ro_down, min_ro_down, max_ro_down,
  		kanisotropy_down, min_kanisotropy_down, max_kanisotropy_down,
  		synt_ro_by_phases, synt_ro_by_ampl,
  		misfit);

      env->ReleaseIntArrayElements(num_probe, (jint*) num_probeC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(md, mdC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(tvd, tvdC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(x, xC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(zeni, zeniC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(ro_by_phases, ro_by_phasesC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(ro_by_ampl, ro_by_amplC, JNI_ABORT);
      env->ReleaseDoubleArrayElements(synt_ro_by_phasesJ, synt_ro_by_phases, 0);
      env->ReleaseDoubleArrayElements(synt_ro_by_amplJ, synt_ro_by_ampl, 0);

      jclass outputModelClass = env->FindClass("ru/nsu/fit/geodrilling/model/OutputModel");
      jmethodID constructorOutputModel = env->GetMethodID(outputModelClass, "<init>", "(DD[D[DDDDDDDDDDDDDDI)V");

      jobject outputModel = env->NewObject(outputModelClass, constructorOutputModel, min_kanisotropy_down, max_kanisotropy_down,
        synt_ro_by_phasesJ, synt_ro_by_amplJ, misfit, kanisotropy_down, 
        min_ro_down, max_ro_down, ro_down, kanisotropy_up,
        min_kanisotropy_up, max_kanisotropy_up, ro_up, min_ro_up,
        max_ro_up, alpha, tvd_start, status);

    return outputModel;
  }

JNIEXPORT jobject JNICALL Java_ru_nsu_fit_geodrilling_services_lib_NativeLibrary_LoggingHorizontalModel6Param1_15DRo
  (JNIEnv *env, jobject obj, jint nprobes, jintArray num_probe, 
  jint npoints, jdoubleArray tvd, jdoubleArray x, jdoubleArray zeni, 
  jdouble tvd_start, jdouble alpha, jdouble ro_up, jdouble kanisotropy_up, 
  jdouble ro_down, jdouble kanisotropy_down) {
    jdoubleArray synt_ro_by_phasesJ = env->NewDoubleArray(nprobes * npoints);
    jdoubleArray synt_ro_by_amplJ = env->NewDoubleArray(nprobes * npoints);
    
    uint32_t* num_probeC = (uint32_t*) env->GetIntArrayElements(num_probe, NULL);
    jdouble* tvdC = env->GetDoubleArrayElements(tvd, NULL);
    jdouble* xC = env->GetDoubleArrayElements(x, NULL);
    jdouble* zeniC = env->GetDoubleArrayElements(zeni, NULL);
    jdouble* synt_ro_by_phases = env->GetDoubleArrayElements(synt_ro_by_phasesJ, NULL);
    jdouble* synt_ro_by_ampl = env->GetDoubleArrayElements(synt_ro_by_amplJ, NULL);

    jint status = LoggingHorizontalModel6Param1_5DRo(nprobes, num_probeC,
      npoints, tvdC, xC, zeniC,
      tvd_start, alpha, ro_up, kanisotropy_up,
      ro_down, kanisotropy_down, synt_ro_by_phases, synt_ro_by_ampl);

    env->ReleaseIntArrayElements(num_probe, (jint*) num_probeC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(tvd, tvdC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(x, xC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(zeni, zeniC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(synt_ro_by_phasesJ, synt_ro_by_phases, 0);
    env->ReleaseDoubleArrayElements(synt_ro_by_amplJ, synt_ro_by_ampl, 0);

    jclass modelSignalClass = env->FindClass("ru/nsu/fit/geodrilling/model/ModelSignal");
    jmethodID constructorModelSignal = env->GetMethodID(modelSignalClass, "<init>", "(DD[D[DI)V");

    jobject modelSignal = env->NewObject(modelSignalClass, constructorModelSignal, ro_down, kanisotropy_down,
      synt_ro_by_phasesJ, synt_ro_by_amplJ, status);

    return modelSignal;
  }

JNIEXPORT jobject JNICALL Java_ru_nsu_fit_geodrilling_services_lib_NativeLibrary_TargetFunctions
  (JNIEnv *env, jobject obj, jint nprobes, jintArray num_probe, 
  jint npoints, jdoubleArray tvd, jdoubleArray x, jdoubleArray zeni, 
  jint n_tvd_start, jdoubleArray tvd_start, 
  jint n_alpha, jdoubleArray alpha, jint n_ro_up, jdoubleArray ro_up, 
  jint n_kanisotropy_up, jdoubleArray kanisotropy_up, 
  jint n_ro_down, jdoubleArray ro_down, 
  jint n_kanisotropy_down, jdoubleArray kanisotropy_down, jdoubleArray phases, jdoubleArray ampls) {
    jdoubleArray target_functionJ = env->NewDoubleArray(n_tvd_start * n_alpha * n_ro_up * n_ro_down * n_kanisotropy_up * n_kanisotropy_down);
  

    jdouble* target_function = env->GetDoubleArrayElements(target_functionJ, NULL);
    uint32_t* num_probeC = (uint32_t*) env->GetIntArrayElements(num_probe, NULL);
    jdouble* tvdC = env->GetDoubleArrayElements(tvd, NULL);
    jdouble* xC = env->GetDoubleArrayElements(x, NULL);
    jdouble* zeniC = env->GetDoubleArrayElements(zeni, NULL);
    jdouble* tvd_startC = env->GetDoubleArrayElements(tvd_start, NULL);
    jdouble* alphaC = env->GetDoubleArrayElements(alpha, NULL);
    jdouble* ro_upC = env->GetDoubleArrayElements(ro_up, NULL);
    jdouble* kanisotropy_upC = env->GetDoubleArrayElements(kanisotropy_up, NULL);
    jdouble* ro_downC = env->GetDoubleArrayElements(ro_down, NULL);
    jdouble* kanisotropy_downC = env->GetDoubleArrayElements(kanisotropy_down, NULL);
    jdouble* phasesC = env->GetDoubleArrayElements(phases, NULL);
    jdouble* amplsC = env->GetDoubleArrayElements(ampls, NULL);
    jint status = TargetFunctions_ByRo(
      nprobes, num_probeC, npoints, tvdC, xC, zeniC,
      n_tvd_start, tvd_startC, n_alpha, alphaC, n_ro_up, ro_upC,
      n_kanisotropy_up, kanisotropy_upC, n_ro_down, ro_downC,
      n_kanisotropy_down, kanisotropy_downC, phasesC, amplsC,
      target_function
    );
    env->ReleaseIntArrayElements(num_probe, (jint*) num_probeC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(tvd, tvdC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(x, xC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(zeni, zeniC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(tvd_start, tvd_startC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(alpha, alphaC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(ro_up, ro_upC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(kanisotropy_up, kanisotropy_upC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(ro_down, ro_downC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(kanisotropy_down, kanisotropy_downC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(phases, phasesC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(ampls, amplsC, JNI_ABORT);
    env->ReleaseDoubleArrayElements(target_functionJ, target_function, 0);

    jclass AreasEquivalenceClass = env->FindClass("ru/nsu/fit/geodrilling/model/AreasEquivalence");
    jmethodID constructorAreasEquivalence = env->GetMethodID(AreasEquivalenceClass, "<init>", "([DI)V");
    jobject AreasEquivalence = env->NewObject(AreasEquivalenceClass, constructorAreasEquivalence, target_functionJ, status);

    return AreasEquivalence;
}
