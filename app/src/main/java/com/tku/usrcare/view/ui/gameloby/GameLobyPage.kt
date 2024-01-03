
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tku.usrcare.R
import com.tku.usrcare.view.UnityActivity
import com.tku.usrcare.view.WebGameActivity
import com.tku.usrcare.view.component.TitleBox

@Composable
fun GameLobyPage() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleBox(
            color = colorResource(id = R.color.btnBrainGameColor),
            title = stringResource(id = R.string.brain_game),
            icon = painterResource(
                id = R.drawable.ic_game
            )
        )
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Button(
                        onClick = {
                            val intent = Intent(context, UnityActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = buttonColors(
                            containerColor = Color.Transparent,
                        ),
                        border = BorderStroke(3.dp, colorResource(id = R.color.btnBrainGameColor)),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.dino),
                                contentDescription = "圖片",
                                Modifier.size(
                                    width = 100.dp, height = 100.dp
                                )
                                    .drawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color.White, Color.Transparent),
                                            startX = 0f,
                                            endX = size.width * 0.5f
                                        ),
                                        blendMode = androidx.compose.ui.graphics.BlendMode.SrcAtop
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.padding(15.dp))
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Row {
                                    Image(
                                        painter = painterResource(id = R.drawable.i2048),
                                        contentDescription = "",
                                        Modifier
                                            .size(width = 40.dp, height = 40.dp)
                                            .clip(
                                                MaterialTheme.shapes.medium
                                            )
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.fish),
                                        contentDescription = "",
                                        Modifier.size(width = 40.dp, height = 40.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.apple),
                                        contentDescription = "",
                                        Modifier.size(width = 40.dp, height = 40.dp)
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.CenterEnd,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Row {
//                                    Icon(
//                                        Icons.Default.KeyboardArrowDown,
//                                        contentDescription = "icon",
//                                        tint = colorResource(id = R.color.black),
//                                        modifier = Modifier.padding(end = 5.dp).size(40.dp)
//                                    )
                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = "icon",
                                            tint = colorResource(id = R.color.black),
                                            modifier = Modifier
                                                .padding(end = 5.dp)
                                                .size(40.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.padding(15.dp)) }
                item {
                    Button(
                        onClick = {
                            val intent = Intent(context, WebGameActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = buttonColors(
                            containerColor = colorResource(id = R.color.white),
                            contentColor = colorResource(id = R.color.black)
                        ),
                        border = BorderStroke(3.dp, colorResource(id = R.color.btnBrainGameColor)),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.card),
                                contentDescription = "圖片",
                                Modifier.size(
                                    width = 100.dp, height = 100.dp
                                ).drawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color.White, Color.Transparent),
                                            startX = 0f,
                                            endX = size.width * 0.4f
                                        ),
                                        blendMode = androidx.compose.ui.graphics.BlendMode.SrcAtop
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.padding(15.dp))
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Row {
                                    Image(
                                        painter = painterResource(id = R.drawable.puncher),
                                        contentDescription = "",
                                        Modifier
                                            .size(width = 40.dp, height = 40.dp)
                                            .clip(
                                                MaterialTheme.shapes.medium
                                            )
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.sudoku),
                                        contentDescription = "",
                                        Modifier.size(width = 40.dp, height = 40.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.guess),
                                        contentDescription = "",
                                        Modifier.size(width = 40.dp, height = 40.dp)
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.CenterEnd,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Row {
//                                    Icon(
//                                        Icons.Default.KeyboardArrowDown,
//                                        contentDescription = "icon",
//                                        tint = colorResource(id = R.color.black),
//                                        modifier = Modifier.padding(end = 5.dp).size(40.dp)
//                                    )
                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = "icon",
                                            tint = colorResource(id = R.color.black),
                                            modifier = Modifier
                                                .padding(end = 5.dp)
                                                .size(40.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameLobyPagePreview() {
    GameLobyPage()
}