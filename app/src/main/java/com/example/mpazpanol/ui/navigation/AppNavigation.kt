package com.example.mpazpanol.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpazpanol.ui.screen.Login
import com.example.mpazpanol.ui.screen.Register
import com.example.mpazpanol.ui.screen.ScreenPanol
import com.example.mpazpanol.ui.screen.Inventario
import com.example.mpazpanol.ui.screen.Prestamos
import com.example.mpazpanol.ui.screen.Devoluciones
import com.example.mpazpanol.ui.screen.Historial
import com.example.mpazpanol.ui.screen.Principal


object Routes{
    const val APPANOL = "panol"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRINCIPAL = "principal"

    const val INVENTARIO = "inventario"

    const val PRESTAMOS = "prestamos"

    const val DEVOLUCIONES = "devoluciones"

    const val HISTORIAL = "historial"
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController=navController,
        startDestination = Routes.APPANOL
    ){
        composable (Routes.APPANOL) {
            ScreenPanol (
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }
        composable(Routes.LOGIN) {
            Login(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {

                    navController.navigate(Routes.PRINCIPAL) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable (Routes.REGISTER){
            Register(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PRINCIPAL) {
            Principal (
                onInventario = {
                    navController.navigate(Routes.INVENTARIO)
                },
                onPrestamos = {
                    navController.navigate(Routes.PRESTAMOS)
                },
                onDevoluciones = {
                    navController.navigate(Routes.DEVOLUCIONES)
                },
                onHistorial = {
                    navController.navigate(Routes.HISTORIAL)
                }
            )
        }

        composable(Routes.INVENTARIO) {
            Inventario()
        }

        composable(Routes.PRESTAMOS) {
            Prestamos()
        }

        composable(Routes.DEVOLUCIONES) {
            Devoluciones()
        }

        composable(Routes.HISTORIAL) {
            Historial()
        }
    }
}