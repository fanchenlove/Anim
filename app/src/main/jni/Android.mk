LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS:= -llog
TARGET_CPU_API := armeabi
APP_ABI := armeabi
LOCAL_MODULE := anim_decode
LOCAL_SRC_FILES := com_fanchen_anim_jni_BumimiDecode.c \
				   com_fanchen_anim_jni_BumimiDecode.h \
				   base64.h \
				   base64.c \
				   md5.h \
				   md5.c \
				   urlutil.h \
				   urlutil.c
include $(BUILD_SHARED_LIBRARY)
