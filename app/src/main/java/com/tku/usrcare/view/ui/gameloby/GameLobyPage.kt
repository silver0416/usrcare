import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tku.usrcare.R
import com.tku.usrcare.view.UnityActivity
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
            Column {
                Button(
                    onClick = {
                        val intent = Intent(context, UnityActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = buttonColors(
                        containerColor = colorResource(id = R.color.btnBrainGameColor),
                        contentColor = colorResource(id = R.color.btnBrainGameColor)
                    ),
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text = "開始遊戲(Unity)",
                        style = TextStyle(
                            color = colorResource(id = R.color.white),
                            fontSize = 25.sp
                        ),
                        modifier = Modifier.padding(15.dp)
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = buttonColors(
                        containerColor = colorResource(id = R.color.white),
                        contentColor = colorResource(id = R.color.black)
                    ),
                    modifier = Modifier
                        .padding(15.dp),
                    border = BorderStroke(8.dp, colorResource(id = R.color.btnBrainGameColor))
                ) {
                    Text(
                        text = "開始遊戲(WebView)",
                        modifier = Modifier.padding(15.dp),
                        style = TextStyle(
                            fontSize = 25.sp,
                            color = colorResource(id = R.color.black)
                        )
                    )
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