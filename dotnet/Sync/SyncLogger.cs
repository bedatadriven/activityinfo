using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ActivityInfo.Sync
{
    interface SyncLogger
    {
        void log(int level, String text);
    }
}
