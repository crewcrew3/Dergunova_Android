package ru.itis.application7.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.itis.application7.feature.detailinfo.DetailInfoScreen
import ru.itis.application7.feature.listcontent.ListContentScreen

@Composable
fun ApplicationNavHost(
    navController: NavHostController = rememberNavController()
) {
    //Лямбда, переданная в NavHost, в конечном итоге вызывает NavController.create Graph() и возвращает NavGraph.
    //NavGraph можно создать отдельно, а потом передать в NavHost
    NavHost(
        navController = navController,
        startDestination = ListContentRoute
    ) {
        composable<ListContentRoute> {
            ListContentScreen(
                onItemClick = { item ->
                    navController.navigate(
                        route = DetailInfoRoute(cityName = item.cityName),
                    )
                },
            )
        }
        composable<DetailInfoRoute> { backStackEntry ->
            val detailInfo: DetailInfoRoute = backStackEntry.toRoute()
            DetailInfoScreen(
                cityName = detailInfo.cityName,
            )
        }
    }
}

