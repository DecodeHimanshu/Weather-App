package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel


@Composable
fun weatherPage(viewModel : WeatherViewModel) {
    var city by remember {
        mutableStateOf(" ")
    }
    val weatherresult = viewModel.weatherResult.observeAsState()
    val keybordController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {// for search
            //text filed
            OutlinedTextField(modifier = Modifier.weight(1f), value = city, onValueChange = {
                city = it;
            },
                label = {
                    Text(text = "Search for any location")
                }
            )

            IconButton(onClick = {
                keybordController?.hide()
                viewModel.getdata(city)
            })
            {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }

        }
        when (val result = weatherresult.value) {
           is NetworkResponse.Error<*> -> {
               Text(text = result.message.toString())
           }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                weatherDeatils(data = result.data)
            }

            null -> {}
        }
    }
}


@Composable
fun  weatherDeatils(data:WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom

        )

        {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location on", modifier = Modifier.size(40.dp))
            Text(text = data.location.region,
                modifier = Modifier.padding(start = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Text(text =",  ${data.location.name} ",
                modifier = Modifier.padding(1.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
                )

        }
        Spacer(modifier = Modifier.height(30.dp))

        //temp in celcius
        Text(text = data.current.temp_c + "° C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(35.dp))
        AsyncImage(modifier = Modifier.size(128.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription ="Condtion Icon "
        )

        Text(text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(45.dp))
    Card {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
               weatherKeyValue(Key ="Humidity" , value =data.current.humidity )
               weatherKeyValue(Key ="Wind Speed" , value =data.current.wind_kph+"Km/h" )

            }

            //2nd row for local time & data
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weatherKeyValue(Key ="Local Time" , value =data.location.localtime.split(" ")[1])
                weatherKeyValue(Key ="Local Date" , value =data.location.localtime.split(" ")[0])

            }

            //#3rd row for
        }
    }
    }

}
@Composable
fun weatherKeyValue(Key:String , value:String) {
    Column (modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = Key,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = value,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray)


    }

}
