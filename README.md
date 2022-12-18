# Ring Sync

Sometimes we leave our phone on silent and forget to switch the ring back on.
Other times its simply just in another room, below a pillow, and who knows where else.
Because of this, I would often miss calls while engrossed in another task on my computer.
This led me develop an application that would help me resolve this issue, in a setting like this.

Ring Sync is a system aimed at reducing the chances of missing calls by notifying and ringing on registered computers during an incoming call.
Admittedly, this is mainly useful when the user happens to be using or atleast located near one of their computers. Nevertheless, this proved to be a useful solution and reduced the chances of missing a call by 60% for me.

As usual, all contributions are gladly welcomed.

## Installation

Before moving on, make sure you have python, pip, and installed. Additionally, install libmpv.so somewhere in your system library search path.

1. Head over to [Releases](https://github.com/kamui-fin/ring-sync/releases) and install the latest APK file onto your Android device.
2. Clone the repository on the computer you are setting up the server on

```sh
git clone https://github.com/kamui-fin/ring-sync.git
cd ring-sync
```

3. Setup the python server and run it with optionally, a custom port
4. Repeat steps 2 and 3 for each device that you would like to receive notifications

```sh
pip install -r requirements.txt
python main.py -p 8888
```

To ensure ring-sync is always running, you should run the server on bootup by adding the command above to your system startup script.

## Usage

Open the android application and set it as your default phone screener when prompted. Then, simply add the server by clicking the `+` button and fill out the corresponding information (IP, port).

After everything is configured, phone calls should now be notifying all the servers you have added.

## License

Distributed under the GPLv3 License. See `LICENSE` for more information.
