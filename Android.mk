#
# SPDX-FileCopyrightText: 2017-2018 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_USE_AAPT2 := true

LOCAL_PACKAGE_NAME := Backgrounds

LOCAL_PRODUCT_MODULE := true

LOCAL_AAPT_FLAGS := --auto-add-overlay

LOCAL_SDK_VERSION := current

include $(BUILD_PACKAGE)
