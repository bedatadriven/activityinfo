using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ActivityInfo.Api
{
    public class SyncRegionUpdates 
    {
        public string Version { get; set; }
        public bool Complete { get; set; } 
        public List<SyncRegionUpdate> Sql { get; set; }
    }

    public class SyncRegionUpdate
    {
        public string Statement { get; set; }
        public List<ParameterSet> Executions { get; set; }

    }

    public class ParameterSet : List<String>
    {


    }
}
