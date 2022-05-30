package com.team404.foodtrack.utils

import android.widget.ImageButton
import androidx.camera.core.Camera
import com.team404.foodtrack.R

object FlashImplementation {
    fun flashListener(camera : Camera, flashButton : ImageButton){
        flashButton.setOnClickListener {
            if(camera.cameraInfo.torchState.value == 0){
                camera.cameraControl.enableTorch(true)
                flashButton.setImageResource(R.drawable.ic_flash_on)
            }else if(camera.cameraInfo.torchState.value == 1){
                camera.cameraControl.enableTorch(false)
                flashButton.setImageResource(R.drawable.ic_flash_off)
            }
        }
    }
}