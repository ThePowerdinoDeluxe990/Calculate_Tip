package com.example.myapplication

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.NumberFormat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}


@Composable
fun TipTimeLayout(){
    var roundUp by remember {
        mutableStateOf(false)
    }

    var amountInput by remember{
        mutableStateOf("")
    }

    var tipInput by remember{
        mutableStateOf("")
    }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = CalculateTip(amount,tipPercent,roundUp)

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text= stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        EditNumberField(
            value = amountInput,
            label = R.string.bill_amount,
            onValueChange = { amountInput = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        EditNumberField(
            value = tipInput,
            label = R.string.how_was_the_service,
            onValueChange = {tipInput = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),

            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        RoundTheTipRow(
            roundUp = roundUp ,
            OnRoundUpChanged = { roundUp = it},
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text= stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))

    }
}

@Composable
fun EditNumberField(
    @StringRes label:Int,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    value:String,
    onValueChange: (String)-> Unit,

    ){
    TextField(
        label = { Text(stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = Modifier
    )
}

@Composable
fun RoundTheTipRow(
    roundUp:Boolean,
    OnRoundUpChanged: (Boolean) -> Unit,
    modifier:Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(stringResource(R.string.round_up_tip))

        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = OnRoundUpChanged,
        )
    }
}

@VisibleForTesting
internal fun CalculateTip(amount:Double, tipPercent:Double = 15.0,roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount

    if(roundUp){
        tip = kotlin.math.ceil(tip)
    }

    return NumberFormat.getCurrencyInstance().format(tip)

}

@Preview(showBackground = true)
@Composable
fun PreviewComposable(){
    MyApplicationTheme{
        TipTimeLayout()
    }
}
