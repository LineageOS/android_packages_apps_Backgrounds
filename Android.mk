#
# Copyright (C) 2017-2018 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

ifeq ($(TARGET_SCREEN_WIDTH),)
    $(warning TARGET_SCREEN_WIDTH is not set, using default value: 1080)
    TARGET_SCREEN_WIDTH := 1080
endif
ifeq ($(TARGET_SCREEN_HEIGHT),)
    $(warning TARGET_SCREEN_HEIGHT is not set, using default value: 1920)
    TARGET_SCREEN_HEIGHT := 1920
endif

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

ifeq ($(shell test $(TARGET_SCREEN_WIDTH) -gt 1080; echo $$?),0)
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1440p/common
ifeq ($(PRODUCT_SIZE), mini)
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1440p/small
else
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1440p/full
endif
else
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1080p/common
ifeq ($(PRODUCT_SIZE), mini)
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1080p/small
else
LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res_1080p/full
endif
endif

LOCAL_USE_AAPT2 := true

LOCAL_PACKAGE_NAME := Backgrounds

LOCAL_AAPT_FLAGS := --auto-add-overlay

LOCAL_SDK_VERSION := current

include $(BUILD_PACKAGE)
