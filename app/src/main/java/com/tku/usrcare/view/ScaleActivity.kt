package com.tku.usrcare.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tku.usrcare.R
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.TitleBox
import com.tku.usrcare.view.ui.scale.FinalFragment
import com.tku.usrcare.view.ui.scale.MoodPuncherButton
import com.tku.usrcare.view.ui.scale.MoodPuncherEditorPage
import com.tku.usrcare.view.ui.scale.MoodPuncherPage
import com.tku.usrcare.view.ui.scale.Scale
import com.tku.usrcare.view.ui.scale.ScaleList
import com.tku.usrcare.viewmodel.ScaleViewModel
import com.tku.usrcare.viewmodel.ViewModelFactory


class ScaleActivity : ComponentActivity() {
    private lateinit var scaleViewModel: ScaleViewModel

    private var startVoiceInput = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            scaleViewModel.saveMoodNowText(data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: "")
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(SessionManager(this))
        scaleViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ScaleViewModel::class.java]
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    ScaleNav(navController = navController)
                }
            }
        }
    }
    sealed class ScaleScreen(val route: String) {
        data object ScaleList : ScaleScreen("ScaleList")
        data object Scale : ScaleScreen("Scale/{id}")
        data object MoodPuncher : ScaleScreen("MoodPuncher")
        data object MoodPuncherEditor : ScaleScreen("MoodPuncherEditor")
        data object CameraCapture : ScaleScreen("CameraCapture")
        data object Final : ScaleScreen("Final/{result}")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun ScaleNav(navController: NavHostController) {
        val context = LocalContext.current

        NavHost(navController, startDestination = ScaleScreen.ScaleList.route,
            enterTransition = {
                scaleIn(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialScale = 0.8f
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    targetScale = 1.2f
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialScale = 1.2f
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    targetScale = 0.8f
                ) + fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.bgScale))
        ) {
            composable(ScaleScreen.ScaleList.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                ) {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.bgScale)
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.2f)
                        ) {
                            TitleBox(
                                color = colorResource(id = R.color.btnMoodScaleColor),
                                title = stringResource(id = R.string.mood_scale),
                                icon = painterResource(id = R.drawable.ic_moodscale)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.6f)
                        ) {
                            ScaleList(navController = navController)
                        }
                        Box(//這是一條線
                            modifier = Modifier
                                .height(3.dp)
                                .fillMaxWidth()
                                .background(color = colorResource(id = R.color.btnMoodScaleColor))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            MoodPuncherButton(navController = navController)
                        }
                    }
                }
            }
            composable(ScaleScreen.Scale.route) {
                window.statusBarColor = ContextCompat.getColor(context, R.color.white)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.white))
                ) {
                    Scale(
                        id = it.arguments?.getString("id")!!.toInt(),
                        navController = navController
                    )
                }
            }
            composable(ScaleScreen.MoodPuncher.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                    contentAlignment = Alignment.Center
                ) {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.bgScale)
                    Column {
                        TitleBox(
                            color = colorResource(id = R.color.btnMoodScaleColor),
                            title = stringResource(id = R.string.mood_puncher),
                            icon = painterResource(id = R.drawable.ic_mood_puncher),
                            navHostController = navController
                        )
                        MoodPuncherPage(navController,scaleViewModel)
                    }
                }
            }
            composable(ScaleScreen.MoodPuncherEditor.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                    contentAlignment = Alignment.Center
                ) {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.bgScale)
                    Column {
                        Box(modifier = Modifier.fillMaxHeight(0.15f)) {
                            TitleBox(
                                color = colorResource(id = R.color.btnMoodScaleColor),
                                title = stringResource(id = R.string.mood_puncher),
                                icon = painterResource(id = R.drawable.ic_mood_puncher),
                                navHostController = navController
                            )
                        }
                        Box(modifier = Modifier.fillMaxHeight()) {
                            MoodPuncherEditorPage(
                                navController,
                                scaleViewModel,
                                startVoiceInput
                            )
                        }
                    }
                }
            }
            composable(ScaleScreen.CameraCapture.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                    contentAlignment = Alignment.Center
                ) {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.bgScale)
                    Column {
                        Box(modifier = Modifier.fillMaxHeight(0.15f)) {
                            TitleBox(
                                color = colorResource(id = R.color.btnMoodScaleColor),
                                title = stringResource(id = R.string.mood_puncher),
                                icon = painterResource(id = R.drawable.ic_mood_puncher),
                                navHostController = navController
                            )
                        }
                        Box(modifier = Modifier.fillMaxHeight()) {
//                            CameraCapture(scaleViewModel = scaleViewModel , navHostController =  navController)
                        }
                    }
                }
            }
            composable (ScaleScreen.Final.route) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgScale)),
                    contentAlignment = Alignment.Center
                ) {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.bgScale)
                    FinalFragment(result = it.arguments?.getString("result")!! , navHostController = navController)
                }
            }
        }
    }
}