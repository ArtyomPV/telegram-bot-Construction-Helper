package ru.prusov.TelegramBotConstructionHelper.constants;

public class TextConstants {

    public static final String RESOURCE_PATH = "TelegramBotConstructionHelper/src/main/resources/images/";
    public static final String LOGO_IMAGE_PATH = "logo.jpg";

    public static final String UNKNOWN_START_MESSAGE = "Я вас не понял. Начните с /start";
    public static final String START_MESSAGE = """
            👋 Приветствую вас в нашем строительном помощнике!
            🏡 Мы — СК «Дом на Холме», и мы специализируемся на строительстве качественных и уютных частных домов. Здесь вы можете:
            1. Узнать о наших услугах и проектах.
            2. Получить предварительный расчет стоимости строительства.
            3. Задать любые вопросы о процессе и материалах.
            4. Оставить заявку на консультацию.
            🤔 Как я могу помочь вам сегодня?
            """;
    public static final String CONSTRUCTION_MESSAGE = """
            В данном разделе можно:
             - посмотреть реализованные объекты
             - почитать статьи
             - для получения детальной информации можно связаться с нами
            """ ;
    public static final String ENGINEERING_MESSAGE = "раздел по инженерным сетям находится в разработке";
    public static final String AUTOMATIZATION_MESSAGE = "раздел по автоматике и системам управления находится в разработке";
    public static final String LOAD_LOGO_MESSAGE = "Загрузите новое изображение логотипа ";
    public static final String NO_CONSTRUCTION_ITEM = "В данном разделе отсутствует информация о построенный объектах";
    public static final String ASK_TITLE_CONSTRUCTION_ITEM = "Введите наименование построенного объекта";
    public static final String ASK_TITLE_ARTICLE_ITEM = "Введите заголовок статьи";
    public static final String ASK_TITLE_ARTICLE_DESCRIPTION = "Введите текст статьи";
    public static final String ASK_DESCRIPTION_CONSTRUCTION_ITEM = "Введите описание построенного объекта";
    public static final String ASK_PHOTO_CONSTRUCTION_ITEM = "Загрузите изображение построенного объекта";
    public static final String MAIN_SETTINGS_MESSAGE = "Настройка контента";
    public static final String CONSTRUCTION_MENU_CONTACTS = """
            
            
            
            Для получения консультации 📃
            
            свяжитесь с менеджером:
            &#43;7(911)484-46-13 <i><b>%s</b></i>
            
            
            """;
}
