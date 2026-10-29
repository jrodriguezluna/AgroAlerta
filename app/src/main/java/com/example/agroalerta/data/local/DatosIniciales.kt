package com.example.agroalerta.data.local

import com.example.agroalerta.utils.hashPassword

suspend fun crearSupervisorPorDefecto(dao: UsuarioDao) {
    var email = "supervisor@agroalerta.cl"
    if (dao.existeEmail(email) == 0) {
        dao.registrar(
            UsuarioEntity(
                nombre = "Supervisor AgroAlerta",
                email = email,
                password = hashPassword("supervisor123"),
                rol = Rol.SUPERVISOR.name
            )
        )
    }
}