package com.gimnasio.service;

import com.gimnasio.model.ChatMessage;
import com.gimnasio.model.User;
import com.gimnasio.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrainerService {

    private final ChatMessageRepository chatMessageRepository;

    private static final Map<String, String[]> RESPUESTAS = Map.of(
        "hola", new String[]{
            "Hola! Soy tu entrenador virtual. En que puedo ayudarte hoy?",
            "Puedes preguntarme sobre ejercicios, rutinas, nutricion o tus objetivos fitness."
        },
        "rutina", new String[]{
            "Para armar una buena rutina, primero dime: cual es tu objetivo? (ganar masa, perder peso, tonificar)",
            "Tambien es importante saber cuantos dias a la semana puedes entrenar."
        },
        "perder peso", new String[]{
            "Para perder peso te recomiendo: 1) Cardio 3-4 veces por semana, 2) Entrenamiento de fuerza 2-3 veces, 3) Mantener calorias en déficit ligero (300-500 cal bajo tu mantenimiento).",
            "La clave es la consistencia. No busques resultados rapidos, busca resultados permanentes."
        },
        "ganar masa", new String[]{
            "Para ganar masa muscular: 1) Entrenamiento de fuerza progresivo, 2) Superavit calorico (200-300 cal sobre tu mantenimiento), 3) 1.6-2.2g de proteina por kg de peso corporal.",
            "El descanso es igual de importante que el entrenamiento. Duerme 7-9 horas."
        },
        "ejercicio", new String[]{
            "Cual grupo muscular quieres trabajar? Pecho, espalda, piernas, hombros, brazos o abdomen?",
            "Recuerda calentar siempre antes de entrenar y mantener buena tecnica."
        },
        "pecho", new String[]{
            "Rutina de pecho recomendada: 1) Press de banca 4x8, 2) Press inclinado 3x10, 3) Aperturas 3x12, 4) Fondos 3x12. Descansa 60-90 seg entre series.",
            "Tip: Concéntrate en la contracción del pecho, no solo en mover el peso."
        },
        "espalda", new String[]{
            "Rutina de espalda: 1) Dominadas o jalón 4x8, 2) Remo con barra 4x8, 3) Remo unilateral 3x10, 4) Face pulls 3x15.",
            "La espalda es un grupo grande, dale el volumen que merece."
        },
        "piernas", new String[]{
            "Rutina de piernas: 1) Sentadilla 4x8, 2) Prensa 3x12, 3) Curl femoral 3x12, 4) Extensión 3x12, 5) Elevación de talones 4x15.",
            "Nunca saltes el dia de piernas! Es donde se genera la mayor respuesta hormonal."
        },
        "calorias", new String[]{
            "Para calcular tus calorias de mantenimiento, multiplica tu peso en kg por 30-35. Luego ajusta segun tu objetivo.",
            "Recuerda que las calorias quemadas en ejercicio varian segun intensidad y duracion."
        },
        "consejo", new String[]{
            "Mi consejo del dia: La constancia vence al talento. Es mejor entrenar regularmente a intensamente de vez en cuando.",
            "Recuerda: hidratate bien, come suficiente proteina y duerme bien."
        }
    );

    public TrainerService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getConversation(Long userId) {
        return chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }

    @Transactional
    public ChatMessage sendMessage(Long userId, User user, String mensaje) {
        ChatMessage userMsg = new ChatMessage(mensaje, "user", user);
        chatMessageRepository.save(userMsg);

        String respuesta = generateResponse(mensaje);
        ChatMessage botMsg = new ChatMessage(respuesta, "trainer", user);
        chatMessageRepository.save(botMsg);

        return botMsg;
    }

    private String generateResponse(String input) {
        String lower = input.toLowerCase().trim();

        for (Map.Entry<String, String[]> entry : RESPUESTAS.entrySet()) {
            if (lower.contains(entry.getKey())) {
                String[] respuestas = entry.getValue();
                int idx = (int) (Math.random() * respuestas.length);
                return respuestas[idx];
            }
        }

        List<String> defaults = new ArrayList<>();
        defaults.add("Interesante pregunta. Para darte la mejor respuesta, necesito que me cuentes mas sobre tu objetivo fitness.");
        defaults.add("Puedo ayudarte con eso. Cuentame mas detalles: que buscas lograr y que nivel de experiencia tienes?");
        defaults.add("Buena pregunta! Para personalizar mi respuesta, dime: cual es tu objetivo principal? (perder peso, ganar masa, mejorar resistencia)");
        defaults.add("Estoy aqui para ayudarte. Prueba preguntarme sobre rutinas, nutricion, ejercicios especificos o consejos generales.");

        int idx = (int) (Math.random() * defaults.size());
        return defaults.get(idx);
    }

    public int getMessageCount(Long userId) {
        return chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId).size();
    }
}
