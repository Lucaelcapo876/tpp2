package ar.edu.ungs.billetera;

import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BilleteAr implements IBilletera {
    private Map<String, Usuario> usuarios;
    private Map<String, Empresa> empresas;
    private ArrayList<Actividad> historialGlobal;
    private int generadorIdInversiones = 1;

    public BilleteAr() {
        this.usuarios = new HashMap<>();
        this.empresas = new HashMap<>();
        this.historialGlobal = new ArrayList<>();
    }
	
	@Override
	public void registrarEmpresa(String cuit, String nombreFantasia, String telefono, String email, String nombreContacto) {
	    
	    // Los campos no pueden ser nulos ni vacios
	    if (cuit == null || cuit.isEmpty() || nombreFantasia == null || nombreFantasia.isEmpty() || 
	        telefono == null || telefono.isEmpty() || email == null || email.isEmpty() || 
	        nombreContacto == null || nombreContacto.isEmpty()) {
	        	throw new IllegalArgumentException("Error: Ningún dato de la empresa puede ser nulo o vacío.");
	    }

	    // La empresa no debe estar registrada previamente
	    if (this.empresas.containsKey(cuit)) {
	        throw new IllegalArgumentException("Error: La empresa con CUIT " + cuit + " ya se encuentra registrada en el sistema.");
	    }

	    // Instanciar la nueva empresa
	    Empresa nuevaEmpresa = new Empresa(cuit, nombreFantasia, telefono, email, nombreContacto);

	    // Guardar la empresa en el sistema
	    this.empresas.put(cuit, nuevaEmpresa);
	}

	@Override
	public void agregarPersonaAutorizada(String cuitEmpresa, String dniAutorizado) {
	    // Validar que la empresa exista en el mapa de empresas
	    if (!this.empresas.containsKey(cuitEmpresa)) {
	        throw new IllegalArgumentException("La empresa con CUIT " + cuitEmpresa + " no existe.");
	    }
	    // Obtener la empresa y agregamos DNI al HashSet<String>
	    Empresa empresa = this.empresas.get(cuitEmpresa);
	    
	    // Podemos lanzar un error si la persona y ase encuentra autorizada
	    if (empresa.estaAutorizado(dniAutorizado)) {
	         throw new IllegalArgumentException("La persona ya se encuentra autorizada.");
	    }
	    
	    empresa.agregarAutorizado(dniAutorizado);
	}

	@Override
	public void registrarUsuario(String dni, String nombre, String telefono, String email) {
	    if(dni == null || dni.isEmpty() || nombre == null || nombre.isEmpty() || telefono == null || 
	            telefono.isEmpty() || email == null || email.isEmpty()) {
	        throw new IllegalArgumentException("Error: Ningún dato del usuario puede ser nulo o vacío.");
	    }
	    
	    if (this.usuarios.containsKey(dni)) {
	        throw new IllegalArgumentException("Error: El usuario con DNI " + dni + " ya se encuentra registrado.");
	    }
	    
	    Usuario nuevoUsuario = new Usuario(nombre, dni, email, telefono);
	    this.usuarios.put(dni, nuevoUsuario);
	}

    // ------------------ GESTIÓN DE CUENTAS ------------------

    private boolean existeAlias(String alias) {
        for (Usuario u : this.usuarios.values()) {
            for (Cuenta c : u.getCuentas().values()) {
                if (c.getAlias().equals(alias)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String crearCuentaRegular(String dniUsuario, String alias) {
        // Validamos que el usuario exista
        if (!this.usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("Error: El usuario con DNI " + dniUsuario + " no existe.");
        }

        // Validamos que el alias no este registrado
        if (existeAlias(alias)) {
            throw new IllegalArgumentException("Error: El alias '" + alias + "' ya se encuentra registrado.");
        }

        // 3. Generar el CVU con la clase de la cátedra
        String nuevoCvu = Utilitarios.generarSiguienteCvu();

        // 4. Instanciar la cuenta (Saldo inicial 0.0 para la regular)
        Cuenta nuevaCuenta = new CuentaRegular(nuevoCvu, alias, 0.0, dniUsuario);

        // 5. Vincular la cuenta al usuario
        Usuario usuario = this.usuarios.get(dniUsuario);
        usuario.agregarCuenta(nuevaCuenta);

        // 6. Retornar el CVU generado
        return nuevoCvu;
    }

    @Override
	public String crearCuentaPremium(String dniUsuario, String alias, double depositoInicial) {
	    if (!this.usuarios.containsKey(dniUsuario)) {
	        throw new IllegalArgumentException("Error: El usuario con DNI " + dniUsuario + " no existe.");
	    }

	    if (existeAlias(alias)) {
	        throw new IllegalArgumentException("Error: El alias '" + alias + "' ya se encuentra registrado.");
	    }

	    String nuevoCvu = Utilitarios.generarSiguienteCvu();
	    CuentaPremium nuevaCuenta = new CuentaPremium(nuevoCvu, alias, depositoInicial, dniUsuario);

	    Usuario usuario = this.usuarios.get(dniUsuario);
	    usuario.agregarCuenta(nuevaCuenta);
	    
	    return nuevoCvu;
	}

	@Override
	public String crearCuentaCorporativa(String dniUsuario, String alias, String cuitEmpresa) {
	    if (!this.usuarios.containsKey(dniUsuario)) {
	        throw new IllegalArgumentException("Error: El usuario con DNI " + dniUsuario + " no existe.");
	    }

	    if (existeAlias(alias)) {
	        throw new IllegalArgumentException("Error: El alias '" + alias + "' ya se encuentra registrado.");
	    }
	    
	    if (!this.empresas.containsKey(cuitEmpresa)) {
	        throw new IllegalArgumentException("Error: La empresa con CUIT " + cuitEmpresa + " no se encuentra registrada en el sistema.");
	    }

	    String nuevoCvu = Utilitarios.generarSiguienteCvu();
	    Cuenta nuevaCuenta = new CuentaCorporativa(nuevoCvu, alias, 0.0, cuitEmpresa, dniUsuario, dniUsuario);


	    Usuario usuario = this.usuarios.get(dniUsuario);
	    usuario.agregarCuenta(nuevaCuenta);

	    return nuevoCvu;
	}

    @Override
	public List<String> obtenerCuentas(String dniUsuario) {
	    // Validamos que el usuario exista
	    if (!this.usuarios.containsKey(dniUsuario)) {
	        throw new IllegalArgumentException("Error: El usuario con DNI " + dniUsuario + " no existe.");
	    }
	    
	    List<String> listaCuentas = new ArrayList<>();
	    Usuario u = this.usuarios.get(dniUsuario);
	    
	    // Recorremos las cuentas del usuario y usamos el toString()
	    for (Cuenta c : u.getCuentas().values()) {
	        listaCuentas.add(c.toString());
	    }
	    
	    return listaCuentas;
	}

    @Override
	public String consultarCvu(String alias) {
	    // Recorremos todos los usuarios del sistema
	    for (Usuario u : this.usuarios.values()) {
	        // Recorremos las cuentas de cada usuario
	        for (Cuenta c : u.getCuentas().values()) {
	            // Si el alias coincide, devolvemos inmediatamente su CVU
	            if (c.getAlias().equals(alias)) {
	                return c.getCvu();
	            }
	        }
	    }
	    
	    // Si terminamos de revisar todas las cuentas y no lo encontramos, lanzamos un error
	    throw new IllegalArgumentException("Error: El alias '" + alias + "' no se encuentra registrado en el sistema.");
	}

    // ------------------ OPERACIONES FINANCIERAS ------------------

    @Override
	public double obtenerSaldoDisponible(String cvu) {
	    // Recorremos todos los usuarios del sistema
	    for (Usuario u : this.usuarios.values()) {
	        // Recorremos las cuentas de cada usuario
	        for (Cuenta c : u.getCuentas().values()) {
	            // Si encontramos la cuenta con el CVU buscado, devolvemos su saldo
	            if (c.getCvu().equals(cvu)) {
	                return c.obtenerSaldo();
	            }
	        }
	    }
	    
	    // Si terminamos de revisar todas las cuentas y no lo encontramos, lanzamos el error
	    throw new IllegalArgumentException("Error: La cuenta con CVU " + cvu + " no existe en el sistema.");
	}

    // Método auxiliar
 	private Cuenta buscarCuentaPorCvu(String cvu) {
 	    for (Usuario u : this.usuarios.values()) {
 	        for (Cuenta c : u.getCuentas().values()) {
 	            if (c.getCvu().equals(cvu)) {
 	                return c;
 	            }
 	        }
 	    }
 	    // Si termina de revisar todo y no lo encuentra, lanza el error
 	    throw new IllegalArgumentException("Error: La cuenta con CVU " + cvu + " no existe.");
 	}

 	@Override
 	public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
 	    // Buscamos ambas cuentas¿
 	    Cuenta cuentaOrigen = buscarCuentaPorCvu(cvuOrigen);
 	    Cuenta cuentaDestino = buscarCuentaPorCvu(cvuDestino);

 	    // Ejecutamos la operación delegando la responsabilidad a los objetos
 	    // El metodo transferir() validara internamente los saldos
 	    cuentaOrigen.transferir(cuentaDestino, monto);

 	    // Instanciamos la actividad usando la clase Transferencia
 	    Transferencia nuevaTransferencia = new Transferencia(cuentaOrigen, cuentaDestino, monto);

 	    // Registramos la actividad en los historiales de las cuentas involucradas
 	    cuentaOrigen.registrarActividad(nuevaTransferencia);
 	    cuentaDestino.registrarActividad(nuevaTransferencia);

 	    // Guardamos la actividad en el historial global del sistema definido en tu diagrama UML
 	    this.historialGlobal.add(nuevaTransferencia);
 	}

    // ------------------ INVERSIONES ------------------

	// Metodo auxiliar
 	private Cuenta buscarCuentaDeUsuario(String dni, String cvu) {
 	    Usuario u = this.usuarios.get(dni);
 	    if (u == null) {
 	        throw new IllegalArgumentException("Error: El usuario con DNI " + dni + " no existe.");
 	    }
 	    for (Cuenta c : u.getCuentas().values()) {
 	        if (c.getCvu().equals(cvu)) {
 	            return c;
 	        }
 	    }
 	    throw new IllegalArgumentException("Error: La cuenta no existe o no pertenece a este usuario.");
 	}
 	
 	@Override
	public int realizarInversionRentaFija(String dni, String cvu, double monto, int plazoDias) {
	    Cuenta cuentaOrigen = buscarCuentaDeUsuario(dni, cvu);

	    if (monto <= 0 || cuentaOrigen.obtenerSaldo() < monto) {
	        throw new IllegalArgumentException("Error: Monto inválido o saldo insuficiente.");
	    }

	    // Descontamos el dinero del saldo
	    cuentaOrigen.setSaldo(cuentaOrigen.obtenerSaldo() - monto);

	    // Obtenemos el ID unico
	    int idInversion = this.generadorIdInversiones++;
	    
	    // Instanciamos la Inversion
	    RentaFija nuevaInversion = new RentaFija(Utilitarios.hoy(), plazoDias, idInversion, 
	    		monto, true, 40.0);

	    // Guardamos la inversion en la cuenta
	    cuentaOrigen.getInversiones().add(nuevaInversion);

	    // Registramos la actividad
	    ConstitucionInversion nuevaActividad = new ConstitucionInversion(cuentaOrigen, nuevaInversion, monto);
	    cuentaOrigen.registrarActividad(nuevaActividad);
	    this.historialGlobal.add(nuevaActividad);

	    return idInversion;
	}

	@Override
	public int realizarInversionLiquidez(String dni, String cvu, double monto, int plazoDias) {
	    Cuenta cuentaOrigen = buscarCuentaDeUsuario(dni, cvu);
	    if (!(cuentaOrigen instanceof CuentaCorporativa)) {
	        throw new IllegalArgumentException("Error: El Fondo de Liquidez Empresarial solo está disponible para Cuentas Corporativas.");
	    }

	    // Validacion del IREP - monto >= 20.000.000
	    if (monto < 20000000.0) {
	        throw new IllegalArgumentException("Error: El monto mínimo para el Fondo de Liquidez es de 20.000.000.");
	    }

	    if (cuentaOrigen.obtenerSaldo() < monto) {
	        throw new IllegalArgumentException("Error: Saldo insuficiente para la inversión.");
	    }

	    cuentaOrigen.setSaldo(cuentaOrigen.obtenerSaldo() - monto);

	    int idInversion = this.generadorIdInversiones++;
	    FondoLiquidezEmpresarial nuevaInversion = new FondoLiquidezEmpresarial(Utilitarios.hoy(), plazoDias, idInversion, monto);


	    cuentaOrigen.getInversiones().add(nuevaInversion);

	    ConstitucionInversion nuevaActividad = new ConstitucionInversion(cuentaOrigen, nuevaInversion, monto);
	    cuentaOrigen.registrarActividad(nuevaActividad);
	    this.historialGlobal.add(nuevaActividad);

	    return idInversion;
	}

	@Override
	public int realizarInversionDivisa(String dni, String cvu, double monto, int plazoDias, String divisa, double tasa) {
	    Cuenta cuentaOrigen = buscarCuentaDeUsuario(dni, cvu);

	    if (monto <= 0 || cuentaOrigen.obtenerSaldo() < monto) {
	        throw new IllegalArgumentException("Error: Monto inválido o saldo insuficiente.");
	    }

	    cuentaOrigen.setSaldo(cuentaOrigen.obtenerSaldo() - monto);

	    int idInversion = this.generadorIdInversiones++;
	    VinculadaDivisa nuevaInversion = new VinculadaDivisa(Utilitarios.hoy(), plazoDias, idInversion, 
	    		monto,true, divisa, tasa);

	    cuentaOrigen.getInversiones().add(nuevaInversion);

	    ConstitucionInversion nuevaActividad = new ConstitucionInversion(cuentaOrigen, nuevaInversion, Double.valueOf(monto));
	    cuentaOrigen.registrarActividad(nuevaActividad);
	    this.historialGlobal.add(nuevaActividad);

	    return idInversion;
	}

	@Override
	public void precancelarInversion(String dni, String cvu, int idInversion) {
	    // Buscamos el usuario y validamos que la cuenta le pertenezca
	    Usuario u = this.usuarios.get(dni);
	    if (u == null || !u.getCuentas().containsKey(cvu)) {
	        throw new IllegalArgumentException("Error: Usuario o cuenta inexistente.");
	    }
	    Cuenta cuenta = u.getCuentas().get(cvu);

	    // Buscamos la inversión por su ID en la lista
	    Inversion inversionEncontrada = null;
	    for (Inversion inv : cuenta.getInversiones()) {
	        if (inv.getId() == idInversion) {
	            inversionEncontrada = inv;
	            break;
	        }
	    }

	    if (inversionEncontrada == null) {
	        throw new IllegalArgumentException("Error: La inversión no existe.");
	    }

	    // Validamos que la inversión no haya finalizado su plazo
	    LocalDate fechaVencimiento = inversionEncontrada.getFechaConstitucion().plusDays(inversionEncontrada.getPlazo());
	    if (!Utilitarios.hoy().isBefore(fechaVencimiento)) {
	        throw new IllegalArgumentException("Error: La inversión ya venció, no está activa.");
	    }

	    if (!inversionEncontrada.getPrecancelable()) {
	        throw new IllegalArgumentException("Error: La inversión no permite ser precancelada.");
	    }

	    // Calculamos los días restando los valores absolutos con toEpochDay()
	    long diasTranscurridos = Utilitarios.hoy().toEpochDay() - inversionEncontrada.getFechaConstitucion().toEpochDay();

	    double interesTotalAnual = inversionEncontrada.calcularResultado();
	    String tipo = inversionEncontrada.getClass().getSimpleName();

	    if (tipo.equals("RentaFija")) {
	        interesTotalAnual = inversionEncontrada.getMontoInvertido() * 0.10; 
	    } else if (tipo.equals("VinculadaDivisa")) {
	        interesTotalAnual = inversionEncontrada.getMontoInvertido() * 1.1109; 
	    }

	    // Calculamos la proporción, depositamos y borramos
	    double interesGanado = (interesTotalAnual / 365.0) * diasTranscurridos;
	    double montoADevolver = inversionEncontrada.getMontoInvertido() + interesGanado;

	    cuenta.depositar(montoADevolver);
	    cuenta.getInversiones().remove(inversionEncontrada);
	}

    // ------------------ REPORTES Y ANÁLISIS ------------------

	@Override
	public List<String> consultarHistorialGlobal() {
	    List<String> historial = new ArrayList<>();
	    
	    // Recorremos el historial global de la billetera
	    for (Actividad actividad : this.historialGlobal) {
	        historial.add(actividad.obtenerDetalle());
	    }
	    
	    return historial;
	}

	@Override
	public List<String> consultarHistorialCuenta(String cvu) {
	    Cuenta cuenta = buscarCuentaPorCvu(cvu); 
	    List<String> historial = new ArrayList<>();
	    
	    for (Actividad actividad : cuenta.getHistorial()) { 
	        historial.add(actividad.obtenerDetalle());
	    }
	    
	    return historial;
	}

	@Override
	public List<String> consultarHistorialUsuario(String dniUsuario) {
	    // Validamos que el usuario exista
	    if (!this.usuarios.containsKey(dniUsuario)) {
	        throw new IllegalArgumentException("Error: El usuario no existe.");
	    }
	    
	    Usuario u = this.usuarios.get(dniUsuario);
	    List<String> historial = new ArrayList<>();
	    
	    // Recorremos todas las cuentas del usuario y luego el historial de cada una
	    for (Cuenta c : u.getCuentas().values()) {
	        for (Actividad actividad : c.getHistorial()) {
	            historial.add(actividad.obtenerDetalle());
	        }
	    }
	    
	    return historial;
	}

	@Override
	public double obtenerTotalInvertido(String dniUsuario) {
	    if (!this.usuarios.containsKey(dniUsuario)) {
	        throw new IllegalArgumentException("Error: El usuario con DNI " + dniUsuario + " no existe.");
	    }

	    Usuario u = this.usuarios.get(dniUsuario);
	    double totalInvertido = 0.0;

	    // Recorremos todas las cuentas del usuario
	    for (Cuenta c : u.getCuentas().values()) {
	        // Recorremos todas las inversiones de cada cuenta
	        for (Inversion inv : c.getInversiones()) {
	            // Sumamos el monto invertido
	            totalInvertido += inv.getMontoInvertido();
	        }
	    }

	    return totalInvertido;
	}

	@Override
	public List<String> cuentasConMayorVolumen(int cantidadTop) {
	    // Validamos la regla estricta de la interfaz
	    if (cantidadTop <= 0) {
	        throw new IllegalArgumentException("Error: La cantidad tope debe ser un número positivo.");
	    }

	    // Recopilamos absolutamente todas las cuentas del sistema en una sola lista
	    List<Cuenta> todasLasCuentas = new ArrayList<>();
	    for (Usuario u : this.usuarios.values()) {
	        todasLasCuentas.addAll(u.getCuentas().values());
	    }

	    // Ordenamos la lista de mayor a menor según el tamaño de su historial
	    // (Usamos el método sort con una pequeña función lambda para comparar)
	    todasLasCuentas.sort((cuenta1, cuenta2) -> 
	        Integer.compare(cuenta2.getHistorial().size(), cuenta1.getHistorial().size())
	    );

	    // Armamos la lista final tomando solo los primeros 3 elementos
	    List<String> topCuentas = new ArrayList<>();
	    
	    // Usamos Math.min por si piden un Top 10 pero en el sistema solo hay 3 cuentas creadas
	    int limite = Math.min(cantidadTop, todasLasCuentas.size());

	    for (int i = 0; i < limite; i++) {
	        // llama al toString() correcto (Premium, Regular o Corporativa)
	        topCuentas.add(todasLasCuentas.get(i).toString());
	    }

	    return topCuentas;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("=== Estado Interno de Billete.ar ===\n");
	    
	    if (this.usuarios == null || this.usuarios.isEmpty()) {
	        sb.append("No hay usuarios registrados en el sistema.\n");
	    } else {
	        for (Usuario u : this.usuarios.values()) {
	            sb.append(u.toString()).append("\n"); // Esto llama al toString() de cada Usuario
	        }
	    }
	    
	    return sb.toString();
	}
}