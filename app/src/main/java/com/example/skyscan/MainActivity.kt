package com.example.skyscan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyscan.api.Current
import com.example.skyscan.navigation.Destinations
import com.example.skyscan.navigation.navhost
import com.example.skyscan.ui.theme.SkyScanTheme

import com.example.skyscan.viewmodel.getweathericon

import com.example.skyscan.viewmodel.viewmodel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {



            SkyScanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController= rememberNavController()

                    navhost(navController = navController)



                }

                }
            }
        }
    }

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun weatherdata(navController: NavHostController){
    var long by remember {
        mutableStateOf("0")
    }
    var lati by remember {
        mutableStateOf("0")
    }
    val fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(LocalContext.current)


    if (ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }
    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
        long= it.longitude.toString()
        lati=it.latitude.toString()




    }
    var isloading by remember{
        mutableStateOf(false)}
    LaunchedEffect(isloading){
        if(isloading){
            delay(2000)
            isloading=false

        }
    }
    if(long!="0"&&lati!="0"){

            weatherscreen(long = long, lati = lati, navController)
        }

}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun weatherscreen(long:String,lati:String,navController: NavHostController){
    var scrollBehavior=TopAppBarDefaults.pinnedScrollBehavior()
    var cityname by
        mutableStateOf("")

    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("CityName")?.observe(navController.currentBackStackEntry!!){
        cityname=it


        }
    navController.currentBackStackEntry?.savedStateHandle!!.remove<String>("CityName")
    var showmenu by remember {
        mutableStateOf(false)
    }
    var checked by remember { mutableStateOf(false) }





    val viewModel = viewModel<viewmodel>(

        factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {



                return viewmodel("$lati,$long") as T



            }
        }
    )
    viewModel.getweatherdata()

    if(cityname!=""){

        viewModel.userlocation=cityname
        viewModel.getweatherdata()

    }

    if(viewModel.isloading==true){
        Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator()
        }
    }
    else{
        Scaffold(modifier= Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            CenterAlignedTopAppBar(navigationIcon = {
                IconButton(onClick = {
                    cityname=""
                    viewModel.userlocation="$lati,$long"
                    viewModel.getweatherdata()




                }) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Get Current location"
                    )

                }
            },
                title = {
                    Text(text = viewModel.cityname, style  = MaterialTheme.typography.titleLarge)


                },
                actions = {
                    IconButton(onClick = { navController.navigate(Destinations.search.route)
                    }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                    Switch(checked = checked, onCheckedChange ={
                        checked=it
                    }, thumbContent = {
                        if(checked){
                            Text(text = "°C", style = MaterialTheme.typography.labelSmall)
                        }
                        if(!checked){
                            Text(text = "°F", style = MaterialTheme.typography.labelSmall)

                        }
                    })
                    
                },
                scrollBehavior = scrollBehavior




            )
        }

        ) {
            if(showmenu){

            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)){
                item{



                    Image(painter = painterResource(id = viewModel.imagecode), contentDescription = "Sunny", modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp), alignment = Alignment.Center)

                    if(checked){
                        Text(text = "${viewModel.currenttemperature_F}°F", style = MaterialTheme.typography.headlineLarge,modifier=Modifier.fillMaxSize(), textAlign = TextAlign.Center)
                    }
                    else {

                        Text(
                            text = "${viewModel.currenttemperature}°C",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )
                    }
                        Text(text = "${viewModel.des}", style = MaterialTheme.typography.headlineLarge,modifier=Modifier.fillMaxSize(), textAlign = TextAlign.Center)

                    Card (modifier= Modifier

                        .padding(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),




                            ),



                        ){
                        Row(modifier = Modifier

                            .padding(15.dp), horizontalArrangement = Arrangement.Center) {



                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.weight(1f))
                                Image(

                                    painter = painterResource(id = R.drawable.sunrise),
                                    contentDescription = "SunRise",
                                    contentScale = ContentScale.Fit,


                                    alignment = Alignment.Center


                                )
                                Text("Sunrise", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                                Text(
                                    text = "${viewModel.sunrise}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )




                            }
                            Spacer(modifier = Modifier.weight(2f))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.sunset),
                                    contentDescription = "Sunset",
                                    contentScale = ContentScale.Fit
                                )
                                Text("Sunrise", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                                Text(
                                    text = "${viewModel.sunset}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )




                            }

                        }

                    }
                    Card (modifier= Modifier

                        .padding(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        ),

                        ){

                        LazyRow(modifier= Modifier
                            .fillMaxSize()
                            .padding()){
                            items(viewModel.hourlyForecasts.size){


                                Column(modifier= Modifier
                                    .fillMaxSize()
                                    .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${
                                            viewModel.hourlyForecasts[it].time.substring(
                                                viewModel.hourlyForecasts[0].time.length - 5
                                            )
                                        }",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 10.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    var icon = getweathericon(
                                        viewModel.hourlyForecasts[it].isday,
                                        viewModel.hourlyForecasts[it].conditionText,
                                    )
                                    Image(
                                        painter = painterResource(id = icon),
                                        contentDescription = "logo",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .padding(bottom = 10.dp)
                                            .size(width = 50.dp, height = 50.dp)
                                    )
                                    if (checked) {
                                        Text(
                                            text = "${viewModel.hourlyForecasts[it].tempFahrenheit.toInt()}°F",
                                            modifier = Modifier.fillMaxSize(),
                                            textAlign = TextAlign.Center
                                        )

                                    } else {
                                        Text(
                                            text = "${viewModel.hourlyForecasts[it].tempCelsius.toInt()}°C",
                                            modifier = Modifier.fillMaxSize(),
                                            textAlign = TextAlign.Center
                                        )


                                    }
                                }






                            }



                        }

                    }
                    Card (modifier= Modifier

                        .padding(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        ),

                        ){

                        Row(modifier= Modifier
                            .fillMaxSize()
                            .padding(10.dp),){
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Feels Like",
                                    modifier = Modifier.padding(bottom = 10.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.feels),
                                    contentDescription = "logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(width = 40.dp, height = 40.dp)
                                )
                                if (checked) {
                                    Text(
                                        text = viewModel.feelslike_F + "°F", modifier = Modifier,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                } else {
                                    Text(
                                        text = viewModel.feelslike + "°C", modifier = Modifier,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }

                                Spacer(modifier = Modifier.weight(2f))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Humidity",modifier= Modifier.padding(bottom = 10.dp),
                                        style=MaterialTheme.typography.bodyLarge)
                                    Image(painter =  painterResource(id = R.drawable.humi), contentDescription = "logo", contentScale = ContentScale.Fit,modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(width = 40.dp, height = 40.dp))
                                    Text(text = viewModel.humidity+"%",modifier= Modifier,
                                        style=MaterialTheme.typography.bodyLarge)
                                }
                            Spacer(modifier = Modifier.weight(2f))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Max Temp", modifier = Modifier.padding(bottom = 10.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.maxtemp),
                                    contentDescription = "logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(width = 40.dp, height = 40.dp)
                                )
                                if (checked) {
                                    Text(
                                        text = viewModel.maxtemp_F + "°F", modifier = Modifier,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                } else {
                                    Text(
                                        text = viewModel.maxtemp + "°C", modifier = Modifier,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(2f))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Min Temp", modifier = Modifier.padding(bottom = 10.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.mintemp),
                                    contentDescription = "logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(width = 40.dp, height = 40.dp)
                                )
                                if (checked) {
                                    Text(
                                        text = viewModel.mintemp_F + "°F", modifier = Modifier,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                } else {
                                    Text(
                                        text = viewModel.mintemp + "°C", modifier = Modifier,
                                        style = MaterialTheme.typography.bodyLarge
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







