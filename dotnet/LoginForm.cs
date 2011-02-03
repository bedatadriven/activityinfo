using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using ActivityInfo.Api;

namespace ActivityInfo
{
    public partial class LoginForm : Form
    {

        private ActivityInfoService service;

        public LoginForm()
        {
            InitializeComponent();

            serverCombo.Text = (string)Application.UserAppDataRegistry.GetValue("Server");
            emailTextBox.Text = (string) Application.UserAppDataRegistry.GetValue("Email");
            passwordTextBox.Text = (string) Application.UserAppDataRegistry.GetValue("Password");
        }


        private void okButton_Click(object sender, EventArgs e)
        {
            try
            {
                String serverUrl = serverCombo.Text;
                if (!serverUrl.EndsWith("/"))
                {
                    serverUrl += "/";
                }
                service = new ActivityInfoService(serverUrl, emailTextBox.Text, passwordTextBox.Text);

                Application.UserAppDataRegistry.SetValue("Server",  serverUrl);
                Application.UserAppDataRegistry.SetValue("Email", emailTextBox.Text);
                Application.UserAppDataRegistry.SetValue("Password", passwordTextBox.Text);

                this.Hide();
            }
            catch (AuthenticationException)
            {
                MessageBox.Show(this, "Invalid Login");
            }

        }

        private void LoginForm_Load(object sender, EventArgs e)
        {

        }

        private void cancelButton_Click(object sender, EventArgs e)
        {
            service = null;
            this.Hide();
        }

        public ActivityInfoService Service { get { return service; } }

        private void label3_Click(object sender, EventArgs e)
        {

        } 
    }
}
