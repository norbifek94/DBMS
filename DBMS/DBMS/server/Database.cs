using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DBMS.backend
{

    class Database
    {
        private String name;
        private List<Table> tabels;

        public Database(String name)
        {
            this.name = name;
        }

    }
}
