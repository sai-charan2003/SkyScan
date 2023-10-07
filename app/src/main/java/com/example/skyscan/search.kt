package com.example.skyscan

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.skyscan.viewmodel.viewmodel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun search(navHostController: NavHostController) {

    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var isclicked by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold {
        DockedSearchBar(query = text, onQueryChange ={ text=it} , onSearch = { navHostController.previousBackStackEntry?.savedStateHandle?.set("CityName", text)
            navHostController.popBackStack()}, active = active, onActiveChange ={ active = it } , modifier= Modifier
            .fillMaxWidth()
            .padding(it), trailingIcon = {
            IconButton(
                onClick = {
                    isclicked=true


                    navHostController.previousBackStackEntry?.savedStateHandle?.set("CityName",text)
                    navHostController.popBackStack()

                }) {
                Icon(imageVector =Icons.Filled.Search , contentDescription ="Search" )

            }
        }) {


        }
    }




}