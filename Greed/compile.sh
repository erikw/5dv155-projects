#!/usr/bin/env bash

ant debug install && adb shell "am start -n se.umu.androidcourse.erwe0033.greed/se.umu.androidcourse.erwe0033.greed.GreedActivity"
