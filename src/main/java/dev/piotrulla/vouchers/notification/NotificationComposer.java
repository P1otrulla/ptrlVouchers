package dev.piotrulla.vouchers.notification;

import com.google.common.base.Joiner;
import dev.piotrulla.vouchers.config.composer.SimpleComposer;
import panda.std.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationComposer implements SimpleComposer<Notification> {

    private static final String SERIALIZE_FORMAT = "[%s]%s";
    private static final Pattern DESERIALIZE_PATTERN = Pattern.compile("\\[([^]]*)](.*)");

    @Override
    public Result<Notification, Exception> deserialize(String source) {
        Matcher matcher = DESERIALIZE_PATTERN.matcher(source);

        if (!matcher.matches()) {
            return Result.error(new IllegalStateException("Can not parse \"" + source + "\" to Notification. Correct format: \"[CHAT]Text to send\""));
        }

        String types = matcher.group(1);
        String message = matcher.group(2);
        List<NotificationType> notificationTypes = new ArrayList<>();

        for (String type : types.replace(" ", "").split(",")) {
            try {
                NotificationType notificationType = NotificationType.valueOf(type);

                notificationTypes.add(notificationType);
            }
            catch (IllegalArgumentException exception) {
                return Result.error(new IllegalStateException("Value '" + type + "' is not a notification type! Available types: " + Joiner.on(", ").join(NotificationType.values())));
            }
        }

        return Result.ok(Notification.of(message, notificationTypes));
    }

    @Override
    public Result<String, Exception> serialize(Notification notification) {
        return Result.ok(String.format(SERIALIZE_FORMAT, Joiner.on(", ").join(notification.types()), notification.message()));
    }
}

