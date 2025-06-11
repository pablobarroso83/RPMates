package com.rpmates
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rpmates.navigation.AppNavigation
import com.rpmates.ui.theme.RPMatesTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
                RPMatesTheme {
                    AppNavigation()
                }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun PreviewText(){
    AppNavigation()
}
