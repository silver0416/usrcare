package com.tku.usrcare.view.ui.scale

//
//@Composable
//fun CameraCapture(scaleViewModel: ScaleViewModel , navHostController: NavHostController) {
//    // 設置拍照的launcher
//    val takePictureLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicturePreview(),
//        onResult = { bitmap ->
//            if (bitmap != null) {
//                // 將Bitmap轉換為InputImage
//                val inputImage = InputImage.fromBitmap(bitmap, 0)
//                recognizeText(inputImage, scaleViewModel , navHostController)
//            }
//        }
//    )
//
//    Button(onClick = {
//        // 啟動拍照
//        takePictureLauncher.launch(null)
//    }) {
//        Text(text = "拍照")
//    }
//
//}

//fun recognizeText(image: InputImage , scaleViewModel: ScaleViewModel , navHostController: NavHostController) {
//    // 初始化文字識別器
//    val recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
//
//    recognizer.process(image)
//        .addOnSuccessListener { visionText ->
//            scaleViewModel.saveMoodNowText(visionText.text)
//            navHostController.navigateUp()
//        }
//        .addOnFailureListener { e ->
//            Toast.makeText(
//                navHostController.context,
//                "文字識別失敗: ${e.message}",
//                Toast.LENGTH_SHORT
//            ).show()
//            navHostController.navigateUp()
//        }
//}