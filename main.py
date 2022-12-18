from flask import Flask, request
from plyer import notification
import argparse
import mpv

app = Flask(__name__)

@app.route("/incoming", methods=["POST"])
def log_incoming():
    content = request.get_json(silent = True)
    name = content.get("name", None)
    number = content["number"]

    title = "Incoming call" if not name else f"{name} is calling you"
    notification.notify(
        title = title,
        message = f"You are receiving a call from {number}",
        app_icon = "phone.ico",
        timeout = 10,
        hints = {"urgency": 2}
    )
    player = mpv.MPV()
    player.play("ringtone.mp3")
    return {"msg": "ok"}, 200

class PortAction(argparse.Action):
    def __call__(self, parser, namespace, values, option_string=None):
        if not 0 < values < 2 ** 16:
            raise argparse.ArgumentError(self, "port numbers must be between 0 and 2**16")
        setattr(namespace, self.dest, values)

if __name__ == '__main__':
    cmd_parser = argparse.ArgumentParser()
    cmd_parser.add_argument('-p',
                        help='Port number to connect to',
                        dest='cmd_port',
                        default=8080,
                        type=int,
                        action=PortAction,
                        metavar="{0..65535}")
    args = cmd_parser.parse_args()
    port = args.port
    app.run(host = "0.0.0.0", port = port)
