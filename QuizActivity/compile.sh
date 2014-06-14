#!/usr/bin/env bash

ant debug && ant installd && adb shell "am start -n com.example.geoquiz/com.example.geoquiz.QuizActivity"
