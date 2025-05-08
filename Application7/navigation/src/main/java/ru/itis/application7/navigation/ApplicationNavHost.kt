package ru.itis.application7.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.itis.application7.authorization.ui.AuthorizationScreen
import ru.itis.application7.core.feature.detailinfo.DetailInfoScreen
import ru.itis.application7.core.feature.listcontent.ListContentScreen
import ru.itis.application7.registration.ui.RegistrationScreen

@Composable
fun ApplicationNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Any,
) {
    //Лямбда, переданная в NavHost, в конечном итоге вызывает NavController.create Graph() и возвращает NavGraph.
    //NavGraph можно создать отдельно, а потом передать в NavHost
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<AuthorizationRoute> {
            AuthorizationScreen(
                toRegistrationScreen = {
                    navController.navigate(
                        route = RegistrationRoute
                    )
                },
                toListContentScreen = {
                    navController.navigate(
                        route = ListContentRoute
                    )
                }
            )
        }
        composable<RegistrationRoute> {
            RegistrationScreen(
                toAuthorizationScreen = {
                    navController.navigate(
                        route = AuthorizationRoute
                    )
                },
                toListContentScreen = {
                    navController.navigate(
                        route = ListContentRoute
                    )
                }
            )
        }
        composable<ListContentRoute> {
            ListContentScreen(
                onItemClick = { item ->
                    navController.navigate(
                        route = DetailInfoRoute(cityName = item.cityName),
                    )
                },
                toAuthorizationScreen = {
                    navController.navigate(
                        route = AuthorizationRoute
                    )
                }
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

