require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: common.js
    module = sys.zb-common

theme: /

    state: Rules
        q!: $regex</start>
        intent!: /Game
        a: Игра больше-меньше. Загадаю число от 0 до 100, ты будешь отгадывать. Начнём?
        go!: /Rules/Agree

        state: Agree

            state: Yes
                intent: /Agreement
                go!: /MakeUpNumber

            state: No
                intent: /Disagreement
                a: Ну и ладно! Если передумаешь - скажи "давай поиграем"

    state: MakeUpNumber
        script:
            $session.number = $jsapi.random(100) + 1;
            #$reactions.answer("Загадано {{$session.number}}");
            $reactions.transition("/CheckNumber");

    state: CheckNumber
        intent!: /Number
        script:
            var num = $parseTree._Number;
            if (num == $session.number) {
                $reactions.answer("Ты выиграл! Хочешь еще раз?");
                $reactions.transition("/Rules/Agree");
            }
            else
                if (num < $session.number)
                    $reactions.answer(selectRandomArg(["Мое число больше!", "Бери выше", "Попробуй число больше"]));
                else $reactions.answer(selectRandomArg(["Мое число меньше!", "Подсказка: число меньше", "Дам тебе еще одну попытку! Мое число меньше."]));

    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Я не понял.
            a: Что вы имеете в виду?
            a: Ничего не пойму

