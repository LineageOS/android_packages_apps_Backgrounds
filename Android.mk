#
# SPDX-FileCopyrightText: 2017-2018 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

ifeq ($(shell test $(TARGET_SCREEN_WIDTH) -gt 1080; echo $$?),0)
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1440p
else
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1080p
endif

LOCAL_USE_AAPT2 := true

LOCAL_PACKAGE_NAME := Backgrounds

LOCAL_PRODUCT_MODULE := true

LOCAL_AAPT_FLAGS := --auto-add-overlay

LOCAL_SDK_VERSION := current

include $(BUILD_PACKAGE)
