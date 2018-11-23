import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame{ //наследование

    //объявление переменных
    private static GameWindow game_window; //для объекта окна
    private static long last_frame_time; //подсчет времени
    private static Image background; //переменная для добавления картинки фона
    private static Image game_over; //переменная для добавления картинки завершения
    private static Image drop; //переменная для добавления картинки капли
    private static Image restart; //переменная для добавления картинки с кнопкой рестарта
    private static float drop_left = 200; //координата х = гориз. - левая граница капли
    private static float drop_top = -100; //координата у = верт. - верхняя граница капли (-100, чтобы капля вылетала из-за границы сверху)
    private static float drop_v = 200; //скорость капли
    private static int score; //счет игры

    //основной метод (все основные действия)
    public static void main(String[] args) throws IOException { //добавляем исключение
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png")); //добавление картинки фона
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png")); //добавление картинки завершения
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png")); //добавление картинки капли
        restart = ImageIO.read(GameWindow.class.getResourceAsStream("restart.png")); //добавление картинки кнопки рестарта
        game_window = new GameWindow(); //создание объекта окна
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //завершение при закрытии
        game_window.setLocation(200, 100); //координаты окна (левый верхний угол)
        game_window.setSize(906, 478); //размер окна
        game_window.setResizable(false); //деактивировать разворот  и изменение развмеров окна
        last_frame_time = System.nanoTime(); //время = текущее время в наносекундах
        GameField game_field = new GameField(); //новый объект класса GameField
        game_field.addMouseListener(new MouseAdapter() { //отлавливаем нажатие на кнопку мыши (вся магия тут)
            @Override
            public void mousePressed(MouseEvent e) { //метод вызывается при нажатии на кноопку мыши
                int x = e.getX(); //координаты нажатия - гориз (из MouseEvent)
                int y = e.getY(); //координаты нажатия - верт (из MouseEvent)
                float drop_right = drop_left + drop.getWidth(null); //считаем правую границу капли: левая границы + ширина капли
                float drop_bottom = drop_top + drop.getHeight(null); //считаем нижнюю границу капли: верхняя граница + высота капли
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                                        //попадает ли точка (Х,У), то есть нажатие, в каплю
                if(is_drop) { //если нажатие в капле
                    drop_top = -100; //откинем каплю наверх, обратно за границу окна
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                                //по горизонтали откидываем в случайное место от 0 до ширины поля - ширина капли
                    drop_v = drop_v + 20; //увеличиваем скорость капли
                    score++; //увеличиваем счет игры на 1
                    game_window.setTitle("Score: " + score); //выводим счет в качестве заголовка окна
                }
            }
        });
        game_window.add(game_field); //добавляем новый объект класса в объект окна
        game_window.setVisible(true); //включить видимость окна
    }

    //настройка окна для "рисования" внутри
    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime(); //объявляем текущее время
        float delta_time = (current_time - last_frame_time) * 0.000000001f; //разница во времени. Умножение - перевод наносек в сек
        last_frame_time = current_time; //время предыдущего кадра = текущее время

        drop_top = drop_top + drop_v * delta_time; //движение капли по вертикали
        g.drawImage(background, 0, 0, null); //фон в окне (фон, с угла 0,0, null - надо)
        g.drawImage(drop, (int) drop_left, (int) drop_top, null); // капля в окне (передаем параметр угла - левая и верхняя точки)
        if (drop_top > game_window.getHeight()) //если верт.граница капли больше (ниже) высоты окна
        {
            g.drawImage(game_over, 280, 120, null); //завершение в окне
            g.drawImage(restart, 700, 300, null); //кнопка рестарта
            game_window.setTitle("Score " + score);
        }
    }

    //панель "рисования"
    private static class GameField extends JPanel{
        @Override
        protected void paintComponent(Graphics g){ //с помощью Graghics рисуется панель. Тут переопределение
            super.paintComponent(g); //отрисовка панели. super - получение доступа к методу paintComponent из JPanel
            onRepaint(g); //вызываем метод "рисования" внутри окна
            repaint(); //для перерисовки (капля движется)
        }
    }

    private static void Restart(){
        //тут надо написать код выполнения рестарта при нажатии на кнопку

    }
}
