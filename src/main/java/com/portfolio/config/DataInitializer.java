package com.portfolio.config;

import com.portfolio.model.Metric;
import com.portfolio.model.User;
import com.portfolio.repository.MetricRepository;
import com.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final MetricRepository metricRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        initializeUsers();
        if (metricRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            // Usuário admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@dashboard.com");
            admin.setName("Administrador");
            admin.setEnabled(true);
            Set<User.Role> adminRoles = new HashSet<>();
            adminRoles.add(User.Role.ADMIN);
            adminRoles.add(User.Role.USER);
            admin.setRoles(adminRoles);
            userRepository.save(admin);
            
            // Usuário comum
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@dashboard.com");
            user.setName("Usuário");
            user.setEnabled(true);
            Set<User.Role> userRoles = new HashSet<>();
            userRoles.add(User.Role.USER);
            user.setRoles(userRoles);
            userRepository.save(user);
        }
    }
    
    private void initializeSampleData() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        
        List<String> categories = Arrays.asList("Vendas", "Marketing", "Suporte", "Desenvolvimento", "Financeiro");
        List<String> colors = Arrays.asList("#4A90E2", "#7B68EE", "#50C878", "#F39C12", "#E74C3C");
        
        String[] metricNames = {
            "Vendas Mensais", "Leads Gerados", "Tickets Resolvidos", 
            "Commits no Git", "Receita Total", "Conversão", "Tráfego Web",
            "Satisfação do Cliente", "Bugs Corrigidos", "Novos Usuários"
        };
        
        for (int i = 0; i < 50; i++) {
            String category = categories.get(random.nextInt(categories.size()));
            String name = metricNames[random.nextInt(metricNames.length)];
            Double value = 100 + random.nextDouble() * 900;
            String color = colors.get(random.nextInt(colors.size()));
            LocalDateTime timestamp = now.minusHours(random.nextInt(168)); // Últimas 7 dias
            
            Metric metric = new Metric();
            metric.setCategory(category);
            metric.setName(name);
            metric.setValue(value);
            metric.setColor(color);
            metric.setTimestamp(timestamp);
            metric.setDescription("Métrica de exemplo para demonstração");
            
            metricRepository.save(metric);
        }
    }
}

