package com.rpmates.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rpmates.screens.CrearPlaylistScreen
import com.rpmates.screens.DetallePlaylistScreen
import com.rpmates.screens.FavoritesScreen
import com.rpmates.screens.HomeScreen
import com.rpmates.screens.LoginScreen
import com.rpmates.screens.SearchScreen
import com.rpmates.screens.SecondScreen
import com.rpmates.screens.SplashScreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rpmates.viewmodel.PlayListViewModel
import com.rpmates.RPMatesApplication
import com.rpmates.screens.RegisterScreen
import com.rpmates.viewmodel.UserViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as RPMatesApplication
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModel.Factory(application.repository)
    )
    val viewModel: PlayListViewModel = viewModel(
        factory = PlayListViewModel.Factory(application.repository, userViewModel)
    )
    
    // Observar el estado del usuario actual
    val currentUser by userViewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) "HomeScreen" else "SplashScreen"
    ) {
        composable("SplashScreen") {
            SplashScreen(navController)
        }

        composable("LoginScreen") {
            // Si el usuario ya está autenticado, redirigir a HomeScreen
            if (currentUser != null) {
                LaunchedEffect(Unit) {
                    navController.navigate("HomeScreen") {
                        popUpTo("LoginScreen") { inclusive = true }
                    }
                }
            }
            LoginScreen(
                onNavigate = { 
                    navController.navigate("HomeScreen") {
                        popUpTo("LoginScreen") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("RegisterScreen") },
                viewModel = userViewModel
            )
        }

        composable("RegisterScreen") {
            // Si el usuario ya está autenticado, redirigir a HomeScreen
            if (currentUser != null) {
                LaunchedEffect(Unit) {
                    navController.navigate("HomeScreen") {
                        popUpTo("RegisterScreen") { inclusive = true }
                    }
                }
            }
            RegisterScreen(
                onBack = { navController.navigateUp() },
                onRegisterSuccess = { 
                    navController.navigate("HomeScreen") {
                        popUpTo("RegisterScreen") { inclusive = true }
                    }
                },
                viewModel = userViewModel
            )
        }

        composable("HomeScreen") {
            HomeScreen(
                onPlaylistClick = { id -> navController.navigate("DetallePlaylistScreen/$id") },
                onCrear = { navController.navigate("CrearScreen") },
                onFavoritesClick = { navController.navigate("FavoritesScreen") },
                onSearchClick = { navController.navigate("SearchScreen") },
                viewModel = viewModel,
                userViewModel = userViewModel
            )
        }

        composable("CrearScreen") {
            CrearPlaylistScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable("FavoritesScreen") {
            FavoritesScreen(
                onBack = { navController.navigateUp() },
                onPlaylistClick = { id -> navController.navigate("DetallePlaylistScreen/$id") },
                viewModel = viewModel,
                userViewModel = userViewModel
            )
        }

        composable("SearchScreen") {
            SearchScreen(
                onBack = { navController.navigateUp() },
                onPlaylistClick = { id -> navController.navigate("DetallePlaylistScreen/$id") },
                viewModel = viewModel,
                userViewModel = userViewModel
            )
        }
        
        composable("DetallePlaylistScreen/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            DetallePlaylistScreen(
                id = id,
                onBack = { navController.navigateUp() },
                viewModel = viewModel,
                userViewModel = userViewModel
            )
        }
    }
}